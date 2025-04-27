class StudentResponse:
    def __init__(self, student_id, subject_name, answers, total_score=0):
        self.student_id = student_id
        self.subject_name = subject_name
        self.answers = answers
        self.total_score = total_score

    def to_dict(self):
        # MongoDB에 저장할 수 있는 딕셔너리 형태로 변환
        return {
            "student_id": self.student_id,
            "subject_name": self.subject_name,
            "answers": self.answers,
            "total_score": self.total_score
        }
