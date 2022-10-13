package com.sozinx.questionnaire.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "QUESTIONS")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long questionId;
    private String question;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scaleId", nullable = false)
    private Scale scale;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Quiz> quizzes;

    public long getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }


}
