package com.yandex.taskTracker.service;
import com.yandex.taskTracker.model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    ArrayList<Task> getHistory();
}
