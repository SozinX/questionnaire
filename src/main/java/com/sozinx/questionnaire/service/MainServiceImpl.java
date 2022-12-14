package com.sozinx.questionnaire.service;

import com.sozinx.questionnaire.models.*;
import com.sozinx.questionnaire.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@SuppressWarnings("unused")
public class MainServiceImpl implements MainService {
    @Autowired
    @SuppressWarnings("unused")
    private ScaleRepository scaleRepository;
    @Autowired
    @SuppressWarnings("unused")
    private RangeRepository rangeRepository;
    @Autowired
    @SuppressWarnings("unused")
    private PatientRepository patientRepository;
    @Autowired
    @SuppressWarnings("unused")
    private QuestionRepository questionRepository;
    @Autowired
    @SuppressWarnings("unused")
    private AnswerRepository answerRepository;
    @Autowired
    @SuppressWarnings("unused")
    private QuizRepository quizRepository;

    @Override
    public Patient savePatient(String firstName, String lastName, String phoneNumber, String dateOfBirth) {
        Patient patient = new Patient(firstName, lastName, phoneNumber, LocalDate.parse(dateOfBirth));
        this.patientRepository.save(patient);
        return patient;
    }

    private Map<String, Integer> buildPoints(Object currentPatientId) {
        List<Quiz> quizzes = quizRepository.findByPatient(findByPatientId(Long.parseLong(currentPatientId.toString())).get(0));
        Map<String, Integer> points = new TreeMap<>();
        quizzes.forEach(q -> {
            points.putIfAbsent(q.getQuestion().getScale().getScaleName(), 0);
            points.put(q.getQuestion().getScale().getScaleName(), points.get(q.getQuestion().getScale().getScaleName()) + q.getAnswer().getPoints());
        });
        return points;
    }

    private Map<String, String> buildStatus(Map<String, Integer> points) {
        Map<String, String> status = new TreeMap<>();
        points.forEach((key, value) -> status.put(key,
                this.rangeRepository.findByScaleAndMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(
                        this.scaleRepository.findByScaleName(key).get(0),
                        value,
                        value).get(0).getStatus().getStatusName()
        ));
        return status;
    }

    private Map<String, Integer> countOfStatuses(Map<String, String> status) {
        Map<String, Integer> counts = new TreeMap<>();
        counts.put("RED", 0);
        counts.put("ORANGE", 0);
        status.forEach((key, value) -> {
            if (Objects.equals(value, "RED") || Objects.equals(value, "ORANGE"))
                counts.put(value, counts.get(value) + 1);

        });
        return counts;
    }

    @Override
    public String generateGeneralResult(Object currentPatientId) {
        Map<String, String> status = this.buildStatus(buildPoints(currentPatientId));
        Map<String, Integer> counts = this.countOfStatuses(status);
        if (counts.get("RED") > 0 || counts.get("ORANGE") >= 3) {
            return "RED";
        } else if (counts.get("ORANGE") == 2) {
            return "ORANGE";
        }
        return "GREEN";
    }

    @Override
    public ArrayList<Result> generateAllResults(Object currentPatientId) {
        Map<String, Integer> points = this.buildPoints(currentPatientId);
        ArrayList<Result> result = new ArrayList<>();
        this.buildStatus(points).forEach((key, value) -> result.add(new Result(key, points.get(key), value)));
        return result;
    }

    @Override
    public List<Patient> findByPatientId(long patientId) {
        return this.patientRepository.findByPatientId(patientId);
    }

    @Override
    public List<Question> findAllQuestions() {
        return (List<Question>) this.questionRepository.findAll();
    }

    @Override
    public List<Question> findByQuestionId(long questionId) {
        return this.questionRepository.findByQuestionId(questionId);
    }

    @Override
    public List<Answer> findAllAnswers() {
        return (List<Answer>) this.answerRepository.findAll();
    }

    @Override
    public List<Answer> findByAnswerId(long answerId) {
        return this.answerRepository.findByAnswerId(answerId);
    }

    @Override
    public void saveAllResults(ArrayList<Quiz> results) {
        this.quizRepository.saveAll(results);
    }


}
