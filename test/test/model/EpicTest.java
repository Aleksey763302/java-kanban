package test.model;

import com.yandex.tracker.model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("Name", "Description");
    }

    @Test
    void addSubtaskId() {
        epic.addSubtaskId(10);
        assertEquals(1, epic.getSubTasksList().size(), "ID подзадачи не добавляется");
    }

    @Test
    void removeSubTaskId() {
        epic.addSubtaskId(10);
        epic.removeSubTaskId(10);
        assertEquals(0, epic.getSubTasksList().size(), "ID подзадачи не удаляется");
    }

    @Test
    void clearSubTaskId() {
        epic.addSubtaskId(10);
        epic.addSubtaskId(11);
        epic.clearSubTaskId();
        assertEquals(0, epic.getSubTasksList().size(), "список ID  не очищается");
    }
}