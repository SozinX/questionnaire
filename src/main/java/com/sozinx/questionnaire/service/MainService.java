package com.sozinx.questionnaire.service;

import com.sozinx.questionnaire.models.*;

import java.util.ArrayList;
import java.util.List;

public interface MainService {
    String generateGeneralResult(Object currentPatientId);

    Patient savePatient(String firstName, String lastName, String phoneNumber, String dateOfBirth);

    List<Patient> findByPatientId(long patientId);

    List<Question> findAllQuestions();

    List<Question> findByQuestionId(long questionId);

    List<Answer> findAllAnswers();

    List<Answer> findByAnswerId(long answerId);

    void saveAllResults(ArrayList<Quiz> results);

    ArrayList<Result> generateAllResults(Object currentPatientId);
}
