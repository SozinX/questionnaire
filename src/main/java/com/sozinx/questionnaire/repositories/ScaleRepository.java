package com.sozinx.questionnaire.repositories;


import com.sozinx.questionnaire.model.Question;
import com.sozinx.questionnaire.model.Scale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScaleRepository extends CrudRepository<Scale, Long> {

}
