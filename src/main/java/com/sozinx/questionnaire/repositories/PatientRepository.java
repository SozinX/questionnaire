package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    List<Patient> findByPatientId(long patientId);
}
