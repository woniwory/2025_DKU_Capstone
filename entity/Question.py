class Question:
    def __init__(self, question_number, question_type, sub_question_number=None, answer=None, point=0):
        self.question_number = question_number
        self.question_type = question_type  # 문제 유형 (short_answer, TF, descriptive 등)
        self.sub_question_number = sub_question_number  # 새끼 문제 번호 (optional)
        self.answer = answer
        self.point = point

    def to_dict(self):
        return {
            "question_number": self.question_number,
            "question_type": self.question_type,
            "sub_question_number": self.sub_question_number,  # 새끼 문제 번호 포함
            "answer": self.answer,
            "point": self.point
        }