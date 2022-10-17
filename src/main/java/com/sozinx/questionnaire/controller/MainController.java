package com.sozinx.questionnaire.controller;

import com.sozinx.questionnaire.models.*;
import com.sozinx.questionnaire.repositories.*;
import com.sozinx.questionnaire.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private MainService mainService;

    @GetMapping(path = "/patient")
    public String patient(Map<String, Object> model) {
        return "patient";
    }

    @PostMapping(path = "/patient")
    public String addNewPatient(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String phoneNumber,
                                @RequestParam String dateOfBirth,
                                Map<String, Object> model,
                                HttpSession session) {
        Patient patient = new Patient(firstName, lastName, phoneNumber, LocalDate.parse(dateOfBirth));
        this.patientRepository.save(patient);
        session.setAttribute("currentPatientId", patient.getPatientId());
        return "redirect:/questionnaire";
    }

    @GetMapping(path = "/questionnaire")
    public String printQuiz(Map<String, Object> model,
                            HttpServletRequest request) {
        Object currentPatientId = request.getSession().getAttribute("currentPatientId");
        if (Objects.equals(currentPatientId, null)) {
            return "redirect:/patient";
        }
        model.put("userId", Long.parseLong(request.getSession().getAttribute("currentPatientId").toString()));
        model.put("questions", questionRepository.findAll());
        model.put("answers", answerRepository.findAll());
        return "questionnaire";
    }

    @PostMapping(path = "/questionnaire")
    public String saveAnswers(HttpServletRequest request,
                              HttpSession session) {
        Object currentPatientId = request.getSession().getAttribute("currentPatientId");
        if (Objects.equals(currentPatientId, null)) {
            return "redirect:/patient";
        }
        ArrayList<Quiz> result = new ArrayList<>();
        String[] questionsIds = request.getParameterValues("questionId");
        for (String id : questionsIds) {
            try {
                result.add(new Quiz(patientRepository.findByPatientId(Long.parseLong(currentPatientId.toString())).get(0),
                        questionRepository.findByQuestionId(Integer.parseInt(id)).get(0),
                        answerRepository.findByAnswerId(Integer.parseInt(request.getParameterValues("question_" + id)[0])).get(0)));
            } catch (NullPointerException e) {
                return "redirect:/questionnaire";
            }
        }
        this.quizRepository.saveAll(result);
        session.setAttribute("isReady", "true");
        return "redirect:/result";
    }

    @GetMapping(path = "/result")
    public String getResults(Map<String, Object> model,
                             HttpServletRequest request) {
        Object currentPatientId = request.getSession().getAttribute("currentPatientId");
        if (Objects.equals(currentPatientId, null)) {
            return "redirect:/patient";
        }
        if (Objects.equals(request.getSession().getAttribute("isReady"), null)) {
            return "redirect:/questionnaire";
        }
        List<Quiz> quizzes = quizRepository.findByPatient(patientRepository.findByPatientId(Long.parseLong(currentPatientId.toString())).get(0));
        Map<String, Integer> points = mainService.buildPoints(quizzes);
        Map<String, String> status = mainService.buildStatus(points);
        String general = mainService.generateGeneralResult(status);
        model.put("general", "General result   =   " + general);
        ArrayList<Result> result = new ArrayList<>();
        status.forEach((key, value) -> result.add(new Result(key, points.get(key), value)));
        model.put("results", result);
        return "result";
    }
}
