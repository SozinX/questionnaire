package com.sozinx.questionnaire.repositories;

import com.sozinx.questionnaire.models.Range;
import com.sozinx.questionnaire.models.Scale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RangeRepository extends CrudRepository<Range, Long> {
    List<Range> findByScaleAndMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(Scale scale, int minPoints, int maxPoints);
}
