package com.sozinx.questionnaire.controller;
import com.sozinx.questionnaire.model.Patients;
import com.sozinx.questionnaire.repositories.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.Collection;

@Controller

public class MainController {

    @Autowired
    private PatientsRepository patientsRepository;

    @GetMapping("/questionnaire")
    public String greeting(Model model) {
        return "questionnaire";
    }

    @PostMapping(path="/add")
    public @ResponseBody String addNewPatient (@RequestParam String firstName,
                                            @RequestParam String lastName,
                                            @RequestParam String phoneNumber,
                                            @RequestParam String dateOfBirth) {


       Patients patient = new Patients();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPhoneNumber(phoneNumber);
       patient.setDateOfBirth(dateOfBirth);
        patientsRepository.save(patient);
        return "Success";
    }
}
