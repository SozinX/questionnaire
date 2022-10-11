package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.model.Patients;
import org.springframework.data.repository.CrudRepository;

public interface PatientsRepository extends CrudRepository<Patients, Long> {

}
