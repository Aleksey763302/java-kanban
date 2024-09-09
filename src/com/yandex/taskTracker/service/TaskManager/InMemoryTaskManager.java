package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Task searchTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);

    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic searchEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void removeEpic(int id) {
        historyManager.remove(id);
        clearEpicSubTasks(id);
        epics.remove(id);
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epics.get(subTask.getEpicId()).addSubtaskId(subTask.getId());
        checkStatus(subTask.getEpicId());
    }

    @Override
    public void clearAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskId();
            checkStatus(epic.getId());
        }
    }

    @Override
    public List<SubTask> getAllSubTasksEpic(int id) {
        ArrayList<Integer> subtasksId = new ArrayList<>(epics.get(id).getSubTasksList());
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer subTaskId : subtasksId) {
            subTasksList.add(subTasks.get(subTaskId));
        }
        return subTasksList;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public SubTask searchSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void removeSubTask(int id) {
        historyManager.remove(id);
        int epicId = subTasks.remove(id).getEpicId();
        epics.get(epicId).removeSubTaskId(id);
        checkStatus(epicId);
    }

    @Override
    public void updateSabTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        int subtaskId = subTask.getId();
        subTasks.put(subtaskId, subTask);
        checkStatus(epicId);
    }

    private void clearEpicSubTasks(int id) {
        ArrayList<Integer> idSubTasks = new ArrayList<>(epics.get(id).getSubTasksList());
        for (Integer subTaskId : idSubTasks) {
            subTasks.remove(subTaskId);
        }
        epics.get(id).clearSubTaskId();
        checkStatus(id);
    }

    private void checkStatus(int id) {
        final TaskStatus DONE = TaskStatus.DONE;
        final TaskStatus IN_PROGRESS = TaskStatus.IN_PROGRESS;
        final TaskStatus NEW = TaskStatus.NEW;
        int countDone = 0;
        int countNew = 0;
        ArrayList<Integer> subtasksId = new ArrayList<>(epics.get(id).getSubTasksList());
        for (Integer subTaskId : subtasksId) {
            if (subTasks.get(subTaskId).getStatus() == DONE) {
                countDone++;
            } else if (subTasks.get(subTaskId).getStatus() == IN_PROGRESS) {
                epics.get(id).setStatus(IN_PROGRESS);
                return;
            } else if (subTasks.get(subTaskId).getStatus() == NEW) {
                countNew++;
            }
        }
        if (countDone > 0 && countNew == 0) {
            epics.get(id).setStatus(DONE);
        } else if (countDone != 0 && countNew != 0) {
            epics.get(id).setStatus(IN_PROGRESS);
        } else {
            epics.get(id).setStatus(NEW);
        }
    }
}