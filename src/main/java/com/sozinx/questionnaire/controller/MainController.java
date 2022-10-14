package com.sozinx.questionnaire.controller;

import com.sozinx.questionnaire.models.*;
import com.sozinx.questionnaire.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

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
    private ScaleRepository scaleRepository;

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
    public String printQuiz(Map<String, Object> model, HttpServletRequest request) {
        if (Objects.equals(request.getSession().getAttribute("currentPatientId"), null)) {
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
    public String saveAnswers(HttpServletRequest request, HttpSession session) {
        if (Objects.equals(request.getSession().getAttribute("currentPatientId"), null)) {
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
        session.setAttribute("isReady", "true");
        return "redirect:/result";
    }

    @GetMapping(path = "/result")
    public String getResults(Map<String, Object> model, HttpServletRequest request) {
        if (Objects.equals(request.getSession().getAttribute("currentPatientId"), null)) {
            return "redirect:/patient";
        }
        if (Objects.equals(request.getSession().getAttribute("isReady"), null)) {
            return "redirect:/questionnaire";
        }
        String general = "GREEN";
        Patient patient = patientRepository.findByPatientId(Long.parseLong(request.getSession().getAttribute("currentPatientId").toString())).get(0);
        List<Quiz> quizzes = quizRepository.findByPatient(patient);
        Set<String> scales = new ConcurrentSkipListSet<>();
        Map<String, Integer> points = new HashMap<>();
        Map<String, String> status = new HashMap<>();
        for (Quiz q : quizzes) {
            scales.add(q.getQuestion().getScale().getName());
            points.putIfAbsent(q.getQuestion().getScale().getName(), 0);
            points.put(q.getQuestion().getScale().getName(), points.get(q.getQuestion().getScale().getName()) + q.getAnswer().getPoints());
            status.put(q.getQuestion().getScale().getName(), "GREEN");
        }
        int redCount = 0;
        int orangeCount = 0;
        for (String s : scales) {
            if (Objects.equals(s, "Vitality")) {
                if (points.get(s) <= 8) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) <= 12) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Social phobia")) {
                if (points.get(s) >= 10) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 7) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Somatic complaints")) {
                if (points.get(s) >= 9) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 6) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Depressed mood")) {
                if (points.get(s) >= 9) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 6) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Cognitive problems")) {
                if (points.get(s) >= 12) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 8) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Anxiety")) {
                if (points.get(s) >= 12) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 8) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Agoraphobia")) {
                if (points.get(s) >= 3) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) == 2) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "Aggression")) {
                if (points.get(s) >= 6) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 4) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
            if (Objects.equals(s, "School/work")) {
                if (points.get(s) >= 12) {
                    status.put(s, "RED");
                    redCount++;
                } else if (points.get(s) >= 8) {
                    status.put(s, "ORANGE");
                    orangeCount++;
                }
            }
        }
        if (redCount > 0 || orangeCount >= 3) {
            general = "RED";
        } else if (orangeCount == 2) {
            general = "ORANGE";
        }
        model.put("general", "General result   =   " + general);
        ArrayList<Result> result = new ArrayList<>();
        for (String s : scales) {
            result.add(new Result(s, points.get(s), status.get(s)));
        }
        model.put("results", result);
        return "result";
    }
}
