package com.sozinx.questionnaire.service;

import com.sozinx.questionnaire.models.Quiz;
import com.sozinx.questionnaire.repositories.RangeRepository;
import com.sozinx.questionnaire.repositories.ScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
public class MainServiceImplement implements MainService {
    @Autowired
    private ScaleRepository scaleRepository;
    @Autowired
    private RangeRepository rangeRepository;

    @Override
    public Map<String, Integer> buildPoints(List<Quiz> quizzes) {
        Map<String, Integer> points = new TreeMap<>();
        quizzes.forEach(q -> {
            points.putIfAbsent(q.getQuestion().getScale().getScaleName(), 0);
            points.put(q.getQuestion().getScale().getScaleName(), points.get(q.getQuestion().getScale().getScaleName()) + q.getAnswer().getPoints());
        });
        return points;
    }

    @Override
    public Map<String, String> buildStatus(Map<String, Integer> points) {
        Map<String, String> status = new TreeMap<>();
        points.forEach((key, value) -> status.put(key,
                rangeRepository.findByScaleAndMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(
                        scaleRepository.findByScaleName(key).get(0),
                        value,
                        value).get(0).getStatus().getStatusName()
        ));
        return status;
    }

    @Override
    public Map<String, Integer> countOfStatuses(Map<String, String> status) {
        Map<String, Integer> counts = new TreeMap<>();
        counts.put("RED", 0);
        counts.put("ORANGE", 0);
        status.forEach((key, value) -> {
            if (Objects.equals(value, "RED") || Objects.equals(value, "ORANGE"))
                counts.put(value, counts.get(value) + 1);

        });
        return counts;
    }
}
