package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.models.Patient;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    List<Patient> findByPatientId(long patientId);

    List<Patient> findByPatientIdGreaterThanAndDateOfQuizEquals(long patientId, LocalDate dateOfQuiz);

    void deleteByPatientId(long id);
}
