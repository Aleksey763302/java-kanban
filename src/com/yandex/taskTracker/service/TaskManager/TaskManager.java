package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;

import java.util.List;
public interface TaskManager {

    List<Task> getHistory();

    void addTask(Task task);

    Task searchTask(int id);

    List<Task> getAllTasks();

    void clearTasks();

    void removeTask(int id);

    void updateTask(Task task);

    void addEpic(Epic epic);

    List<Epic> getAllEpics();

    Epic searchEpic(int id);

    void removeEpic(int id);

    void clearAllEpics();

    void addSubTask(SubTask subTask);

    void clearAllSubtasks();

    List<SubTask> getAllSubTasksEpic(int id);

    List<SubTask> getAllSubTasks();

    SubTask searchSubTask(int id);

    void removeSubTask(int id);

    void updateSabTask(SubTask subTask);
}
