package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.models.Patient;
import com.sozinx.questionnaire.models.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByPatient(Patient patient);
}
