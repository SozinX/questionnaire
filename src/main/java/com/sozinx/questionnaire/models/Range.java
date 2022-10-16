package com.sozinx.questionnaire.models;

import javax.persistence.*;

@Entity
@Table(name = "RANGES")
public class Range {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rangeId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scaleId", nullable = false)
    private Scale scale;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
    private int minPoints;
    private int maxPoints;

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
}
