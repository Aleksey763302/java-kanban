package com.yandex.taskTracker.model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasksId = new ArrayList<>();
    public Epic(String name, String description) {
        super(name,description);
    }
    public ArrayList<Integer> getSubTasksList() {
        return subTasksId;
    }
    public void addSubtaskId(Integer id){
        subTasksId.add(id);
    }
    public void removeSubTaskId(Integer id){
       subTasksId.remove(id);
    }
    public void clearSubTaskId(){
        subTasksId.clear();
    }
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                ", subTasksList.size()=" + subTasksId.size() +
                '}';
    }
}