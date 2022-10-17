package com.sozinx.questionnaire.service;

import com.sozinx.questionnaire.models.Patient;
import com.sozinx.questionnaire.models.Quiz;

import java.util.List;
import java.util.Map;

public interface MainService {
    Map<String, Integer> buildPoints(List<Quiz> quizzes);

    Map<String, String> buildStatus(Map<String, Integer> points);

    Map<String, Integer> countOfStatuses(Map<String, String> status);

    String generateGeneralResult(Map<String, String> status);
}
