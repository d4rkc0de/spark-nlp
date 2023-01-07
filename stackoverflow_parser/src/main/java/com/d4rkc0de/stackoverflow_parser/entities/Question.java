package com.d4rkc0de.stackoverflow_parser.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    private List<String> tags;
    private Owner owner;
    private Boolean is_answered;
    private Integer view_count;
    private Integer answer_count;
    private Integer score;
    private Long last_activity_date;
    private Long creation_date;
    private Long question_id;
    private String content_license;
    private String link;
    private String title;
}
