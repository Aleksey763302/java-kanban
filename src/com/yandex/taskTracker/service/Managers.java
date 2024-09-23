package com.yandex.taskTracker.service;

import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.HistoryManager.InMemoryHistoryManager;
import com.yandex.taskTracker.service.TaskManager.FileBackedTaskManager;
import com.yandex.taskTracker.service.TaskManager.InMemoryTaskManager;
import com.yandex.taskTracker.service.TaskManager.TaskManager;

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