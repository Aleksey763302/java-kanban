package com.yandex.taskTracker.Test.TestService;

import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}