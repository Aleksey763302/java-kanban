package com.yandex.taskTracker.service;

import com.yandex.taskTracker.service.historyManager.HistoryManager;
import com.yandex.taskTracker.service.historyManager.InMemoryHistoryManager;
import com.yandex.taskTracker.service.taskManager.FileBackedTaskManager;
import com.yandex.taskTracker.service.taskManager.InMemoryTaskManager;
import com.yandex.taskTracker.service.taskManager.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}