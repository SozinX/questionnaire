package com.sozinx.questionnaire.controller;

import com.sozinx.questionnaire.models.*;
import com.sozinx.questionnaire.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class MainController {

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
        session.setAttribute("currentPatientId", this.mainService.savePatient(firstName, lastName, phoneNumber, dateOfBirth).getPatientId());
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
        model.put("questions", this.mainService.findAllQuestions());
        model.put("answers", this.mainService.findAllAnswers());
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
                result.add(new Quiz(this.mainService.findByPatientId(Long.parseLong(currentPatientId.toString())).get(0),
                        this.mainService.findByQuestionId(Integer.parseInt(id)).get(0),
                        this.mainService.findByAnswerId(Integer.parseInt(request.getParameterValues("question_" + id)[0])).get(0)));
            } catch (NullPointerException e) {
                return "redirect:/questionnaire";
            }
        }
        this.mainService.saveAllResults(result);
        session.setAttribute("isReady", "true");
        return "redirect:/result";
    }

    @GetMapping(path = "/result")
    public String getResults(Map<String, Object> model,
                             HttpServletRequest request) {
        Object currentPatientId = request.getSession().getAttribute("currentPatientId");
        if (Objects.equals(currentPatientId, null))
            return "redirect:/patient";
        if (Objects.equals(request.getSession().getAttribute("isReady"), null))
            return "redirect:/questionnaire";
        model.put("general", "General result   =   " + this.mainService.generateGeneralResult(currentPatientId));
        model.put("results", this.mainService.generateAllResults(currentPatientId));
        return "result";
    }
}
