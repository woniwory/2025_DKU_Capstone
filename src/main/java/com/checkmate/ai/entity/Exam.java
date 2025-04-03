package com.checkmate.ai.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "exams")
public class Exam {

    private String examName;
    private List<Response> responses;

    // 생성자, getter, setter
}
