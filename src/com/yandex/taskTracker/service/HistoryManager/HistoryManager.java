package com.yandex.taskTracker.service.HistoryManager;

import com.yandex.taskTracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
