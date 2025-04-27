import os


class Config:
    # MongoDB URI 설정
    MONGO_URI = 'mongodb://root:example@localhost:27017/capstone?authSource=admin'
    SECRET_KEY = 'mysecretkey'
    DEBUG = True
    UPLOAD_FOLDER = 'uploads'

    @staticmethod
    def create_upload_folder():
        os.makedirs(Config.UPLOAD_FOLDER, exist_ok=True)
