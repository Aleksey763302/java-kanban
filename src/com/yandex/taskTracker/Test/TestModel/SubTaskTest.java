package com.yandex.taskTracker.Test.TestModel;

import com.yandex.taskTracker.model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
SubTask subtask;
@BeforeEach
public void beforeEach(){
    subtask = new SubTask("Name","Description",100);
}
    @Test
    void getEpicId() {
        final int epicId= subtask.getEpicId();
        assertEquals(subtask.getEpicId(),epicId,"ID отличаются");
    }

    @Test
    void setEpicId() {
        subtask.setEpicId(10);
        assertEquals(10, subtask.getEpicId(), "ID эпика неверный" );
    }
}