package com.sozinx.questionnaire.models;

public class Result {
    private String scale;
    private int points;
    private String status;

    public Result(String scale, int points, String status) {
        this.scale = scale;
        this.points = points;
        this.status = status;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
