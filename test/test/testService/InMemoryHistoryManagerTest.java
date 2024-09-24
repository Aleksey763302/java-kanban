package test.testService;

import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private static HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void getHistory() {
        Task task = new Task("Test getHistory", "Test getHistory description");
        ArrayList<Task> history = new ArrayList<>();
        history.add(task);
        historyManager.add(task);
        List<Task> saveHistory = historyManager.getHistory();
        assertEquals(history, saveHistory, "списки историй отличаются");
    }

    @Test
    void addedTaskInEndListIfWasPreviouslyAdded() {
        Task task1 = new Task("Test addedTaskInEndListIfWasPreviouslyAdded", "task1 description");
        Task task2 = new Task("Test addedTaskInEndListIfWasPreviouslyAdded", "task2 description");
        Task task3 = new Task("Test addedTaskInEndListIfWasPreviouslyAdded", "task3 description");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2);
        final List<Task> history = historyManager.getHistory();
        Task lastTask = history.get(2);
        Task secondTask = history.get(1);
        assertNotNull(history, "История пустая.");
        assertEquals(lastTask.getId(), task2.getId(), "Задача не добавилась");
        assertNotEquals(secondTask.getId(), task2.getId(), "задача не удалилась");
        assertEquals(3, history.size(), "колличество задачь не совподает");
    }

    @Test
    void removeTaskOfHistory() {
        Task task1 = new Task("Test removeTask", "task1 description");
        Task task2 = new Task("Test removeTask", "task2 description");
        Task task3 = new Task("Test removeTask", "task3 description");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> getTask = historyManager.getHistory();
        assertEquals(2, getTask.size(), "размер списка не совпадает");
    }
}