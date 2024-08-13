package com.yandex.taskTracker.service;

import com.yandex.taskTracker.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history = new ArrayList<>();
    @Override
    public ArrayList<Task> getHistory(){
        ArrayList <Task> list = new ArrayList<>();
        for (int i = history.size() - 1; i >= 0; i--) {
            if(list.size() < 10){
                list.add(history.get(i));
            }
        }
        return list;
    }
    @Override
    public void add(Task task) {
        history.add(task);
    }
}