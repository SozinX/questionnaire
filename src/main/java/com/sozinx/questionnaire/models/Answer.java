package com.sozinx.questionnaire.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ANSWERS")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answerId;
    private String answer;
    private int points;
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Quiz> quizzes;

    public long getAnswerId() {
        return answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public int getPoints() {
        return points;
    }

}
