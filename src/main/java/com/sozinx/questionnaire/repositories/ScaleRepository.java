package com.sozinx.questionnaire.repositories;


import com.sozinx.questionnaire.models.Scale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScaleRepository extends CrudRepository<Scale, Long> {
    List<Scale> findByScaleName(String scaleName);
}
