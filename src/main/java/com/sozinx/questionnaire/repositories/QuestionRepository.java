package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.models.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByQuestionId(long questionId);
}
