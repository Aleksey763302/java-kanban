package com.yandex.taskTracker.service.HistoryManager;

import com.yandex.taskTracker.model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        history.addLast(task);
        if (history.size() == 11) {
            history.removeFirst();
        }
    }
}