package com.yandex.taskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, int epicId,
                   int startYear, int startMonth, int startDayOfMonth, int startHour, int startMinute,
                   int duration) {
        super(name, description, startYear, startMonth, startDayOfMonth, startHour, startMinute, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int epicId,
                   LocalDateTime dateTime, Duration duration) {
        super(name, description, dateTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int epicId, int id,
                   int startYear, int startMonth, int startDayOfMonth, int startHour, int startMinute, int duration) {
        super(name, description, startYear, startMonth, startDayOfMonth, startHour, startMinute, duration);
        this.epicId = epicId;
        setId(id);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}