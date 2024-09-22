package test.testService;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class FileBackedTaskManagerTest {
    @Test
    public void mustLoadTasksFromFile() {
        try {
            File file = File.createTempFile("tempFile", ".tmp");
            TaskManager tm = Managers.loadFromFile(file);
            Task task = new Task("Test write in file", "description");
            Task task2 = new Task("Test write in file", "description");
            Epic epic = new Epic("Test write in file", "description");
            tm.addEpic(epic);
            SubTask subTask = new SubTask("Test write in file", "description", epic.getId());
            tm.addSubTask(subTask);
            tm.addTask(task);
            tm.addTask(task2);
            Task saveTask = tm.searchTask(task.getId());
            List<Task> saveTasks = tm.getAllTasks();
            TaskManager tm2 = Managers.loadFromFile(file);
            Task loadTask = tm2.searchTask(task.getId());
            List<Task> loadTasks = tm2.getAllTasks();
            Assertions.assertNotNull(loadTask, "задача не загрузилась");
            Assertions.assertEquals(saveTask, loadTask, "задачи разные");
            Assertions.assertArrayEquals(saveTasks.toArray(), loadTasks.toArray(), "массивы задач не равны");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}