package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.models.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    List<Answer> findByAnswerId(long answerId);
}
