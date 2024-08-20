package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
public interface TaskManager {

    LinkedList<Task> getHistory();

    void addTask(Task task);

    Task searchTask(int id);

    ArrayList<Task> getAllTasks();

    void clearTasks();

    void removeTask(int id);

    void updateTask(Task task);

    void addEpic(Epic epic);

    ArrayList<Epic> getAllEpics();

    Epic searchEpic(int id);

    void removeEpic(int id);

    void clearAllEpics();

    void addSubTask(SubTask subTask);

    void clearAllSubtasks();

    ArrayList<SubTask> getAllSubTasksEpic(int id);

    ArrayList<SubTask> getAllSubTasks();

    SubTask searchSubTask(int id);

    void removeSubTask(int id);

    void updateSabTask(SubTask subTask);
}
