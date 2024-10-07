package test.testService;

import com.yandex.taskTracker.service.historyManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.taskManager.TaskManager;
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