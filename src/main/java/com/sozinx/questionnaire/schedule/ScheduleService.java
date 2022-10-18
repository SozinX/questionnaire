package com.sozinx.questionnaire.schedule;

import com.sozinx.questionnaire.models.Patient;
import com.sozinx.questionnaire.repositories.PatientRepository;
import com.sozinx.questionnaire.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@SuppressWarnings("unused")
public class ScheduleService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private QuizRepository quizRepository;
    static final Logger LOGGER = Logger.getLogger(ScheduleService.class.getName());

    @Scheduled(cron = "${interval-in-cron-every-day}")
    @Transactional
    public void deleteTrash() {
        List<Patient> check = patientRepository.findByDateOfQuizEquals(java.time.LocalDate.now().minus(Period.ofDays(1)));
        if (!check.isEmpty()) {
            check.stream().filter(current -> quizRepository.findByPatient(current).isEmpty()).forEach(delete -> {
                patientRepository.deleteByPatientId(delete.getPatientId());
                LOGGER.log(Level.INFO, "Scheduled msg: Deleted patient with name {0}", delete.getFirstName());
            });
        } else {
            LOGGER.log(Level.INFO, "Scheduled msg: No one took the test yesterday");
        }

    }
}

