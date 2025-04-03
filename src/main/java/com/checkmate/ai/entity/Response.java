package com.checkmate.ai.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Response {
    private int questionNumber;
    private String studentAnswer;
    private int confidence;
    private boolean isCorrect;
    private int score;
}
