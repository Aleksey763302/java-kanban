package test.model;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;

    @BeforeEach
    public void beforeEach() {
        task = new Task("Name", "Description", TaskStatus.NEW,
                LocalDateTime.parse("2024-01-01T12:13"), Duration.ofHours(2), 0);
    }

    @Test
    void getName() {
        String name = task.getName();
        assertEquals("Name", name);
    }

    @Test
    void setName() {
        String saveName = task.getName();
        task.setName("новое имя task");
        assertNotEquals(saveName, task.getName());
    }

    @Test
    void getDescription() {
        String description = task.getDescription();
        assertEquals("Description", description);
    }

    @Test
    void setDescription() {
        String saveDescription = task.getDescription();
        task.setDescription("новое описание task");
        assertNotEquals(saveDescription, task.getDescription());
    }

    @Test
    void getStatus() {
        TaskStatus taskStatus = task.getStatus();
        assertEquals(TaskStatus.NEW, taskStatus);
    }

    @Test
    void setStatus() {
        task.setStatus(TaskStatus.DONE);
        TaskStatus taskStatus = task.getStatus();
        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    void getId() {
        int id = task.getId();
        assertEquals(task.getId(), id);
    }

    @Test
    void setId() {
        int id = task.getId();
        task.setId(20);
        assertNotEquals(task.getId(), id);
    }
}