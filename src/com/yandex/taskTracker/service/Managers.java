package com.yandex.taskTracker.service;

public class Managers  {

    public TaskManager getDefault() {
        return (TaskManager) new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return (HistoryManager) new InMemoryHistoryManager();
    }
}
