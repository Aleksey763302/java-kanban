package test.testService;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;

import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.taskManager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void checkDataTimeTest() {
        Task task = new Task("Test checkDataTimeTest", "description", "2024-01-01T12:13", 2);
        Task task2 = new Task("Test checkDataTimeTest", "description", "2024-01-01T12:30", 2);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        Assertions.assertFalse(taskManager.searchTask(task2.getId()).isPresent(), "задача добавилась при пересесечении");
        Task task3 = new Task("Test checkDataTimeTest", "description", "2024-01-01T14:30", 2);

        taskManager.addTask(task3);
        Assertions.assertTrue(taskManager.searchTask(task3.getId()).isPresent(), "задача не добавилась");
        Task task4 = new Task("Test checkDataTimeTest", "description",
                "2024-01-01T10:13", 3);
        taskManager.addTask(task4);
        Assertions.assertFalse(taskManager.searchTask(task4.getId()).isPresent(), "задача добавилась при пересесечении");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "description", "2024-01-01T12:13", 2);
        taskManager.addTask(task);
        final int taskId = task.getId();
        Task savedTask = null;
        if (taskManager.searchTask(taskId).isPresent()) {
            savedTask = taskManager.searchTask(taskId).get();
        }
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTask() {
        Task task = new Task("Test removeTask", "description", "2024-01-01T12:13", 2);
        taskManager.addTask(task);
        final int taskId = task.getId();
        taskManager.removeTask(taskId);
        assertTrue(taskManager.searchTask(taskId).isEmpty(), "задача не удалилась.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test updateTask", "description", "2024-01-01T12:13", 2);
        taskManager.addTask(task);
        final int taskId = task.getId();
        Task saveTask = null;
        if (taskManager.searchTask(taskId).isPresent()) {
            saveTask = taskManager.searchTask(taskId).get();
        }
        assertNotNull(saveTask, "задача не найдена");
        Task taskUpdate = new Task("Test updateTask", "Test updateTask description",
                TaskStatus.IN_PROGRESS, saveTask.getEndTime().minus(saveTask.getDuration()), saveTask.getDuration(), taskId);
        taskManager.updateTask(taskUpdate);
        Task saveUpdateTask = null;
        if (taskManager.searchTask(taskId).isPresent()) {
            saveUpdateTask = taskManager.searchTask(taskId).get();
        }
        assertNotNull(saveUpdateTask, "обновленная задача не найдена");
        assertNotEquals(saveTask, saveUpdateTask, "Обновление не произошло");
    }

    @Test
    void clearTasks() {
        Task task = new Task("Test updateTask", "description", "2024-01-01T12:13", 2);
        taskManager.addTask(task);
        taskManager.clearTasks();
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "список задач не очистился");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtaskE2", "description", 0, "2024-11-11T22:25", 2);
        taskManager.addSubTask(subTask);
        taskManager.removeSubTask(subTask.getId());
        final int epicId = epic.getId();
        Epic savedEpic = null;
        if (taskManager.searchEpic(epicId).isPresent()) {
            savedEpic = taskManager.searchEpic(epicId).get();
        }
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(0, epic.getSubTasksList().size(), "эпик содержит неактуальные подзадачи");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("Test removeEpic", "Test removeEpic description");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        taskManager.removeEpic(epicId);
        assertTrue(taskManager.searchEpic(epicId).isEmpty(), "эпик не удалился из списка эпиков");
    }

    @Test
    void clearEpicsAndSubtask() {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic description");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("subtaskE2", "description", 0, "2024-11-11T22:25", 2);
        taskManager.addSubTask(subTask1);
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "эпики не удалились");
        assertEquals(0, subTasks.size(), "подзадачи не удалились");
    }

    @Test
    void updateEpicAndSubtask() {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic description");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("subtaskE1", "description", 0, "2024-11-11T22:25", 2);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subtaskE2", "description", 0, "2024-11-13T22:25", 2);
        taskManager.addSubTask(subTask2);
        final int epicId = epic.getId();
        final int subtaskId1 = subTask1.getId();
        final int subtaskId2 = subTask2.getId();
        SubTask subtaskOneUpdate = new SubTask("subtaskE3", "description2", 0, subTask1.getId(),
                "2024-09-09T22:15", 3);
        subtaskOneUpdate.setStatus(TaskStatus.DONE);
        taskManager.updateSabTask(subtaskOneUpdate);
        SubTask searchSubtask = null;
        if (taskManager.searchSubTask(subtaskId1).isPresent()) {
            searchSubtask = taskManager.searchSubTask(subtaskId1).get();
        }
        assertNotNull(searchSubtask, "подзадача не найдена");
        assertEquals(searchSubtask, subtaskOneUpdate, "Обновление подзадачи не выполнено");
        Epic searchEpic = null;
        if (taskManager.searchEpic(epicId).isPresent()) {
            searchEpic = taskManager.searchEpic(epicId).get();
        }
        assertNotNull(searchEpic, "эпик не найден");
        assertEquals(TaskStatus.IN_PROGRESS, searchEpic.getStatus(), "обновление статуса эпика не выполнено");
        SubTask subtaskTwoUpdate = new SubTask("update subtaskE2", "description", 0, subTask2.getId(),
                "2024-11-13T23:25", 2);
        subtaskTwoUpdate.setStatus(TaskStatus.DONE);
        taskManager.updateSabTask(subtaskTwoUpdate);
        SubTask subtaskTwo = null;
        if (taskManager.searchSubTask(subtaskId2).isPresent()) {
            subtaskTwo = taskManager.searchSubTask(subtaskId2).get();
        }
        assertNotNull(subtaskTwo, "вторая подзадача не найдена");
        assertEquals(subtaskTwo, subtaskTwoUpdate, "Обновление подзадачи2 не выполнено");
        assertEquals(TaskStatus.DONE, searchEpic.getStatus(), "обновление статуса эпика не выполнено");
        assertEquals(2, epic.getSubTasksList().size(), "неверное колличество подзадач эпика");
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Test addSubtask", "Test addSubtask description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("update subtaskE2", "description", 0, "2024-11-13T23:25", 2);
        taskManager.addSubTask(subTask);
        final int subtaskId = subTask.getId();
        SubTask savedSubtask = null;
        if (taskManager.searchSubTask(subtaskId).isPresent()) {
            savedSubtask = taskManager.searchSubTask(subtaskId).get();
        }
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubtask, "Подзадачи не совпадают.");
        final List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic("Test removeSubtask", " description");
        taskManager.addEpic(epic);
        SubTask subtask = new SubTask("update subtaskE2", "description", 0, "2024-11-13T23:25", 2);
        taskManager.addSubTask(subtask);
        final int subtaskId = subtask.getId();
        taskManager.removeSubTask(subtaskId);
        Epic searchEpic = null;
        if (taskManager.searchEpic(epic.getId()).isPresent()) {
            searchEpic = taskManager.searchEpic(epic.getId()).get();
        }
        assertNotNull(searchEpic, "эпик не найден");
        List<Integer> id = searchEpic.getSubTasksList();
        assertFalse(id.contains(subtaskId), "подзадача не удалилась из эпика");
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        SubTask searchSubtask = null;
        if (taskManager.searchSubTask(subtaskId).isPresent()) {
            searchSubtask = taskManager.searchSubTask(subtaskId).get();
        }
        assertFalse(subTasks.contains(searchSubtask), "подзадача не удалилась из скиска подзадач");
    }
}