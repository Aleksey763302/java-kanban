package test.testService;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.taskManager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class FileBackedTaskManagerTest {
    @Test
    void mustLoadTasksFromFile() {
        try {
            File file = File.createTempFile("tempFile", ".tmp");
            TaskManager tm = Managers.loadFromFile(file);
            Task task = new Task("mustLoadTasksFromFile", "description",
                    "2024-01-01T12:13", 2);
            Task task2 = new Task("mustLoadTasksFromFile", "description",
                    "2024-01-01T12:13", 2);
            Epic epic = new Epic("mustLoadTasksFromFile", "description");
            tm.addEpic(epic);
            SubTask subTask = new SubTask("subtaskE2", "description", 0,
                    "2024-11-11T22:25", 2);
            tm.addSubTask(subTask);
            tm.addTask(task);
            tm.addTask(task2);
            Task saveTask = null;
            if (tm.searchTask(task.getId()).isPresent()) {
                saveTask = tm.searchTask(task.getId()).get();
            }
            Assertions.assertNotNull(saveTask, "сохраняемая задача не найдена");
            List<Task> saveTasks = tm.getAllTasks();
            TaskManager tm2 = Managers.loadFromFile(file);
            Task loadTask = null;
            if (tm2.searchTask(task.getId()).isPresent()) {
                loadTask = tm2.searchTask(task.getId()).get();
            }
            Assertions.assertNotNull(loadTask, "загружаемая задача не найдена");
            List<Task> loadTasks = tm2.getAllTasks();
            Assertions.assertNotNull(loadTask, "задача не загрузилась");
            Assertions.assertEquals(saveTask, loadTask, "задачи разные");
            Assertions.assertArrayEquals(saveTasks.toArray(), loadTasks.toArray(), "массивы задач не равны");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}