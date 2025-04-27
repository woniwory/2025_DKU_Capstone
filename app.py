import os
import base64
from flask import send_file
from flask_socketio import SocketIO, emit
from flask_cors import CORS
from flask import Flask, request, jsonify
from flask_pymongo import PyMongo

from entity.StudentResponse import StudentResponse
from config import Config

# Flask 애플리케이션 초기화
app = Flask(__name__)


app.config.from_object(Config)
# 업로드 폴더가 존재하지 않으면 생성
Config.create_upload_folder()


mongo = PyMongo(app)
mongo.init_app(app)


# CORS와 WebSocket 활성화
CORS(app)
socketio = SocketIO(app, cors_allowed_origins="*")

# Config 클래스로 설정을 로드


# 이미지 업로드 (HTTP)
@app.route('/upload-image', methods=['POST'])
def upload_image():
    if 'image' not in request.files:
        return jsonify({'error': 'No image file found in request'}), 400

    image = request.files['image']
    if image.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    save_path = os.path.join(app.config['UPLOAD_FOLDER'], image.filename)
    image.save(save_path)

    return jsonify({'message': 'Image uploaded successfully', 'filename': image.filename})

# 메시지 수신 (WebSocket)
@socketio.on('send_message')
def handle_send_message(data):
    message = data.get('message')
    sender = data.get('sender')
    print(f"메시지 수신 - {sender}: {message}")
    emit('message_response', {'message': f'{sender}가 보낸 메시지: {message}'}, broadcast=True)

# 이미지 요청 수신 및 이미지+텍스트 응답 (WebSocket)
@socketio.on('image_request')
def handle_image_request(data):
    filename = data.get('filename')
    filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)

    if os.path.exists(filepath):
        with open(filepath, 'rb') as image_file:
            encoded_image = base64.b64encode(image_file.read()).decode('utf-8')

        # 함께 전송할 텍스트 데이터
        temp_text = f"{filename}은 테스트용 이미지입니다."

        emit('image_response', {
            'image': encoded_image,
            'text_data': temp_text
        })
    else:
        emit('image_response', {
            'error': '파일을 찾을 수 없습니다.'
        })

# 이미지 직접 조회 (옵션 - HTTP GET)
@app.route('/image/<filename>')
def get_image(filename):
    filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    if not os.path.exists(filepath):
        return jsonify({'error': 'File not found'}), 404
    return send_file(filepath)




# StudentResponse 컬렉션
student_response_collection = mongo.db.student_responses


# StudentResponse 생성
@app.route('/student-responses/create', methods=['POST'])
def create_student_response():
    data = request.get_json()

    # 필수 데이터 추출
    student_id = data.get('student_id')
    subject_name = data.get('subject_name')
    answers = data.get('answers')

    if not student_id or not subject_name or not answers:
        return jsonify({"error": "필수 데이터가 누락되었습니다."}), 400

    # StudentResponse 객체 생성
    student_response = StudentResponse(student_id, subject_name, answers)

    # 총점 계산
    total_score = sum(answer['score'] for answer in answers)  # 답변의 점수 합산
    student_response.total_score = total_score

    # MongoDB에 저장
    result = student_response_collection.insert_one(student_response.to_dict())

    # 저장된 ID 반환
    return jsonify({"message": "학생 응답이 성공적으로 생성되었습니다.", "id": str(result.inserted_id)}), 201



if __name__ == '__main__':
    print("서버 실행 중... http://localhost:5000")
    socketio.run(app, host='0.0.0.0', port=5000)
