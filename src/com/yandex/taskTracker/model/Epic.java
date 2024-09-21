package com.yandex.taskTracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
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
                '}';
    }
}