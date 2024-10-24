package test.service;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskStatus;
import com.yandex.tracker.service.history.HistoryManager;
import com.yandex.tracker.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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
        Task task = new Task("Name", "Description", TaskStatus.NEW,
                LocalDateTime.parse("2024-01-01T12:13"), Duration.ofHours(2), 0);
        ArrayList<Task> history = new ArrayList<>();
        history.add(task);
        historyManager.add(task);
        List<Task> saveHistory = historyManager.getHistory();
        assertEquals(history, saveHistory, "списки историй отличаются");
    }

    @Test
    void addedTaskInEndListIfWasPreviouslyAdded() {
        Task task1 = new Task("addedTaskInEndListIfWasPreviouslyAdded", "description",
                "2024-01-01T13:01", 1);
        Task task2 = new Task("addedTaskInEndListIfWasPreviouslyAdded2", "description",
                "2024-02-01T13:01", 1);
        Task task3 = new Task("addedTaskInEndListIfWasPreviouslyAdded3", "description",
                "2024-03-01T13:01", 1);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
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
        Task task1 = new Task("Test removeTaskOfHistory1", "description",
                "2024-01-02T13:01", 2);
        Task task2 = new Task("Test removeTaskOfHistory2", "description",
                "2024-02-03T13:01", 1);
        Task task3 = new Task("Test removeTaskOfHistory3", "description",
                "2024-03-04T13:01", 4);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        List<Task> getTask = historyManager.getHistory();
        assertEquals(2, getTask.size(), "размер списка не совпадает");
    }
}