package com.yandex.taskTracker.Test.TestService;

import com.yandex.taskTracker.service.HistoryManager;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    Managers managers = new Managers();
    @Test
    void getDefault() {
        TaskManager taskManager = managers.getDefault();
        assertNotNull(taskManager);
    }
    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}