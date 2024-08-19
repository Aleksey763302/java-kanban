package com.yandex.taskTracker.service;

import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.HistoryManager.InMemoryHistoryManager;
import com.yandex.taskTracker.service.TaskManager.InMemoryTaskManager;
import com.yandex.taskTracker.service.TaskManager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return (TaskManager) new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return (HistoryManager) new InMemoryHistoryManager();
    }
}
