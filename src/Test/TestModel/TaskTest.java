package Test.TestModel;

import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;

    @BeforeEach
    public void beforeEach() {
        task = new Task("Name", "Description");
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