package com.yandex.taskTracker.Test.TestService;

import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

        ArrayList<Task> saveHistory = historyManager.getHistory();
        assertEquals(history, saveHistory, "списки историй разные");
    }

    @Test
    void addHistory() {
        Task task = new Task("Test addHistory", "Test addHistory description");
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}