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
@SuppressWarnings("unused")
public class MainController {

    private static final String CURRENT_PATIENT_ID = "currentPatientId";
    private static final String IS_READY = "isReady";
    @Autowired
    @SuppressWarnings("unused")
    private MainService mainService;

    @SuppressWarnings("unused")
    @GetMapping(path = "/patient")
    public String patient(Map<String, Object> model) {
        return "patient";
    }

    @SuppressWarnings("unused")
    @PostMapping(path = "/patient")
    public String addNewPatient(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String phoneNumber,
                                @RequestParam String dateOfBirth,
                                Map<String, Object> model,
                                HttpSession session) {
        session.setAttribute(CURRENT_PATIENT_ID, this.mainService.savePatient(firstName, lastName, phoneNumber, dateOfBirth).getPatientId());
        return "redirect:/questionnaire";
    }

    @SuppressWarnings("unused")
    @GetMapping(path = "/questionnaire")
    public String printQuiz(Map<String, Object> model,
                            HttpServletRequest request) {
        Object currentPatientId = request.getSession().getAttribute(CURRENT_PATIENT_ID);
        if (Objects.equals(currentPatientId, null)) {
            return "redirect:/patient";
        }
        model.put("userId", Long.parseLong(request.getSession().getAttribute(CURRENT_PATIENT_ID).toString()));
        model.put("questions", this.mainService.findAllQuestions());
        model.put("answers", this.mainService.findAllAnswers());
        return "questionnaire";
    }

    @SuppressWarnings("unused")
    @PostMapping(path = "/questionnaire")
    public String saveAnswers(HttpServletRequest request,
                              HttpSession session) {
        Object currentPatientId = request.getSession().getAttribute(CURRENT_PATIENT_ID);
        if (Objects.equals(currentPatientId, null)) {
            return "redirect:/patient";
        }
        ArrayList<Quiz> result = new ArrayList<>();
        String[] questionsIds = request.getParameterValues("questionId");
        for (String id : questionsIds) {
            Patient patient = this.mainService.findByPatientId(Long.parseLong(currentPatientId.toString())).get(0);
            Question question = this.mainService.findByQuestionId(Integer.parseInt(id)).get(0);
            Answer answer = this.mainService.findByAnswerId(Integer.parseInt(request.getParameterValues("question_" + id)[0])).get(0);
            if (patient == null || question == null || answer == null) {
                return "redirect:/questionnaire";
            }
            result.add(new Quiz(patient, question, answer));
        }
        this.mainService.saveAllResults(result);
        session.setAttribute(IS_READY, "true");
        return "redirect:/result";
    }

    @SuppressWarnings("unused")
    @GetMapping(path = "/result")
    public String getResults(Map<String, Object> model,
                             HttpServletRequest request) {
        Object currentPatientId = request.getSession().getAttribute(CURRENT_PATIENT_ID);
        if (Objects.equals(currentPatientId, null))
            return "redirect:/patient";
        if (Objects.equals(request.getSession().getAttribute(IS_READY), null))
            return "redirect:/questionnaire";
        model.put("general", "General result   =   " + this.mainService.generateGeneralResult(currentPatientId));
        model.put("results", this.mainService.generateAllResults(currentPatientId));
        return "result";
    }
}
