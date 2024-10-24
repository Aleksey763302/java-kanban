package com.yandex.tracker.service;

import com.yandex.tracker.service.history.HistoryManager;
import com.yandex.tracker.service.history.InMemoryHistoryManager;
import com.yandex.tracker.service.manager.FileBackedTaskManager;
import com.yandex.tracker.service.manager.InMemoryTaskManager;
import com.yandex.tracker.service.manager.TaskManager;

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