class ExamResponse:
    def __init__(self, question_number, student_answer, confidence, is_correct, score, sub_question_number=None):
        self.question_number = question_number
        self.student_answer = student_answer
        self.confidence = confidence
        self.is_correct = is_correct
        self.score = score
        self.sub_question_number = sub_question_number  # 새끼 문제 번호 (optional)

    def to_dict(self):
        return {
            "question_number": self.question_number,
            "student_answer": self.student_answer,
            "confidence": self.confidence,
            "is_correct": self.is_correct,
            "score": self.score,
            "sub_question_number": self.sub_question_number  # 새끼 문제 번호 포함
        }
