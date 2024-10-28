package com.yandex.tracker.service.manager;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.SubTask;
import com.yandex.tracker.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {
    TreeSet<Task> getPrioritizedTasks();

    List<Task> getHistory();

    void addTask(Task task);

    Optional<Task> searchTask(int id);

    List<Task> getAllTasks();

    void clearTasks();

    void removeTask(int id);

    void updateTask(Task task);

    void addEpic(Epic epic);

    List<Epic> getAllEpics();

    Optional<Epic> searchEpic(int id);

    void removeEpic(int id);

    void clearAllEpics();

    void addSubTask(SubTask subTask);

    void clearAllSubtasks();

    List<SubTask> getAllSubTasksEpic(int id);

    List<SubTask> getAllSubTasks();

    Optional<SubTask> searchSubTask(int id);

    void removeSubTask(int id);

    void updateSabTask(SubTask subTask);

    boolean checkTime(Task task);

    Set<Integer> getSetId();

    int giveID();
}