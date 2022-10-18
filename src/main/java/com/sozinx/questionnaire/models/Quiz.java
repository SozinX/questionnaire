package com.sozinx.questionnaire.models;

import javax.persistence.*;

@Entity
@Table(name = "QUIZZES")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recordId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answerId", nullable = false)
    private Answer answer;

    public Quiz() {
    }

    public Quiz(Patient patient, Question question, Answer answer) {
        this.patient = patient;
        this.question = question;
        this.answer = answer;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
