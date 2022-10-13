package com.sozinx.questionnaire.controller;

import com.sozinx.questionnaire.model.Answer;
import com.sozinx.questionnaire.model.Patient;
import com.sozinx.questionnaire.model.Question;
import com.sozinx.questionnaire.model.Quiz;
import com.sozinx.questionnaire.repositories.AnswerRepository;
import com.sozinx.questionnaire.repositories.PatientRepository;
import com.sozinx.questionnaire.repositories.QuestionRepository;
import com.sozinx.questionnaire.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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
        Patient patient = new Patient(firstName, lastName, phoneNumber, dateOfBirth);
        this.patientRepository.save(patient);
        session.setAttribute("currentPatientId", patient.getPatientId());
        return "redirect:/questionnaire";
    }

    @GetMapping(path = "/questionnaire")
    public String printQuiz(Map<String, Object> model, HttpServletRequest request) {
        if (Objects.equals(request.getSession().getAttribute("currentPatientId"),null)) {
            return "redirect:/patient";
        }
        Iterable<Question> questions = questionRepository.findAll();
        Iterable<Answer> answers = answerRepository.findAll();
        model.put("userId", Long.parseLong(request.getSession().getAttribute("currentPatientId").toString()));
        model.put("questions", questions);
        model.put("answers", answers);
        return "questionnaire";
    }

    @PostMapping(path = "/questionnaire")
    public String saveAnswers(HttpServletRequest request) {
        if (Objects.equals(request.getSession().getAttribute("currentPatientId"),null)) {
            return "redirect:/patient";
        }
        ArrayList<Quiz> result = new ArrayList<>();
        String[] questionsIds = request.getParameterValues("questionId");
        for (String id : questionsIds) {
            try {
                Patient patient = patientRepository.findByPatientId(Long.parseLong(request.getSession().getAttribute("currentPatientId").toString())).get(0);
                Question question = questionRepository.findByQuestionId(Integer.parseInt(id)).get(0);
                Answer answer = answerRepository.findByAnswerId(Integer.parseInt(request.getParameterValues("question_" + id)[0])).get(0);
                Quiz quiz = new Quiz(patient, question, answer);
                result.add(quiz);
            } catch (NullPointerException e) {
                return "redirect:/questionnaire";
            }
        }
        this.quizRepository.saveAll(result);
        return "questionnaire";
    }
}
