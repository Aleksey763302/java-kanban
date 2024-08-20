package com.yandex.taskTracker.service.HistoryManager;

import com.yandex.taskTracker.model.Task;

import java.util.LinkedList;

public interface HistoryManager {
    void add(Task task);

    LinkedList<Task> getHistory();
}
