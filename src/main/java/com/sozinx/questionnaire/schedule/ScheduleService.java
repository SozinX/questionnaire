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
    private long id;

    @Scheduled(cron = "${interval-in-cron-every-day}")
    @Transactional
    public void deleteTrash() {
        List<Patient> check = patientRepository.findByPatientIdGreaterThanAndDateOfQuizEquals(id, java.time.LocalDate.now().minus(Period.ofDays(1)));
        try {
            for (Patient c : check) {
                if (quizRepository.findByPatient(c).isEmpty()) {
                    LOGGER.log(Level.INFO, "Scheduled msg: Deleted patient with name {0}", c.getFirstName());
                    patientRepository.deleteByPatientId(c.getPatientId());
                }
            }
            this.id = check.get(check.size() - 1).getPatientId();
            LOGGER.log(Level.INFO, "Scheduled msg: New start ID is {0}", id);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER.log(Level.INFO, "Scheduled msg: No one took the test yesterday");
            LOGGER.log(Level.INFO, "Scheduled msg: ID is  the same - {0}", id);
        }

    }
}

