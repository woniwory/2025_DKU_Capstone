package com.checkmate.ai.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Question {

    private int questionNumber; // 문제 번호

    private String questionText; // 문제 내용

    private String correctAnswer; // 정답 (예: "A", "B" 등)

    private int allocatedScore; // 배점
}