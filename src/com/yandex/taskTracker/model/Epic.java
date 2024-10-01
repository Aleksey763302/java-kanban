package com.yandex.taskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId;

    public Epic(String name, String description, int startYear, int startMonth, int startDayOfMonth, int startHour, int startMinute, int duration) {
        super(name, description, startYear, startMonth, startDayOfMonth, startHour, startMinute, duration);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, LocalDateTime dateTime, Duration duration) {
        super(name, description, dateTime, duration);
        this.subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksList() {
        return subtasksId;
    }

    public void addSubtaskId(Integer id) {
        subtasksId.add(id);
    }

    public void removeSubTaskId(Integer id) {
        subtasksId.remove(id);
    }

    public void clearSubTaskId() {
        subtasksId.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}