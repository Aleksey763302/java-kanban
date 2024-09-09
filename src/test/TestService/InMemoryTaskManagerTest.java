package test.TestService;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;

import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new Managers().getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.searchTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTask() {
        Task task = new Task("Test RemoveTask", "Test RemoveTask description");
        taskManager.addTask(task);
        final int taskId = task.getId();
        taskManager.removeTask(taskId);
        List<Task> taskList = taskManager.getAllTasks();
        assertFalse(taskList.contains(taskId), "задача не удалилась.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test updateTask", "Test updateTask description");
        taskManager.addTask(task);
        final int taskId = task.getId();
        Task saveTask = taskManager.searchTask(taskId);
        Task taskUpdate = new Task("Test updateTask", "Test updateTask description", TaskStatus.IN_PROGRESS, taskId);
        taskManager.updateTask(taskUpdate);
        Task saveUpdateTask = taskManager.searchTask(taskId);
        assertNotEquals(saveTask, saveUpdateTask, "Обновление не произошло");
    }

    @Test
    void clearTasks() {
        Task task = new Task("Test clearAllTasks", "Test clearAllTasks description");
        taskManager.addTask(task);
        taskManager.clearTasks();
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "список задач не очистился");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test addNewEpic", "Test addNewEpic description", epic.getId());
        taskManager.addSubTask(subTask);
        taskManager.removeSubTask(subTask.getId());
        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.searchEpic(epicId);
        assertEquals(0, epic.getSubTasksList().size(), "эпик содержит неактуальные подзадачи");
        assertNotNull(savedEpic, "Эпик не найден.");
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
        List<Epic> epics = taskManager.getAllEpics();
        assertFalse(epics.contains(epicId), "эпик не удалился из списка эпиков");
    }

    @Test
    void clearEpicsAndSubtask() {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic description");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Test updateEpic", "Test description subtask1", epic.getId());
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
        SubTask subTask1 = new SubTask("Test updateEpic", "Test description subtask1", epic.getId());
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Test updateEpic", "Test description subtask2", epic.getId());
        taskManager.addSubTask(subTask2);
        final int epicId = epic.getId();
        final int subtaskId1 = subTask1.getId();
        final int subtaskId2 = subTask2.getId();
        SubTask subtaskOneUpdate = new SubTask("Update subtask", "description update", epicId, subTask1.getId());
        subtaskOneUpdate.setStatus(TaskStatus.DONE);
        taskManager.updateSabTask(subtaskOneUpdate);
        assertEquals(taskManager.searchSubTask(subtaskId1), subtaskOneUpdate, "Обновление подзадачи1 не выполнено");
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.searchEpic(epicId).getStatus(), "обновление статуса эпика не выполнено");
        SubTask subtaskTwoUpdate = new SubTask("Update subtask", "description update", epicId, subTask2.getId());
        subtaskTwoUpdate.setStatus(TaskStatus.DONE);
        taskManager.updateSabTask(subtaskTwoUpdate);
        assertEquals(taskManager.searchSubTask(subtaskId2), subtaskTwoUpdate, "Обновление подзадачи2 не выполнено");
        assertEquals(TaskStatus.DONE, taskManager.searchEpic(epicId).getStatus(), "обновление статуса эпика не выполнено");
        assertEquals(2, epic.getSubTasksList().size(), "неверное колличество подзадач эпика");
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Test addSubtask", "Test addSubtask description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test addSubtask", "Test addSubtask description", epic.getId());
        taskManager.addSubTask(subTask);
        final int subtaskId = subTask.getId();
        final SubTask savedSubtask = taskManager.searchSubTask(subtaskId);
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
        SubTask subtask = new SubTask("Test removeSubtask", " description", epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubTask(subtask);
        final int subtaskId = subtask.getId();
        taskManager.removeSubTask(subtaskId);
        List<Integer> id = taskManager.searchEpic(epic.getId()).getSubTasksList();
        assertFalse(id.contains(subtaskId), "подзадача не удалилась из эпика");
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertFalse(subTasks.contains(subtaskId), "подзадача не удалилась из скиска подзадач");
    }
}