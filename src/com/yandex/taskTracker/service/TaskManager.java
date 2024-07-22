package com.yandex.taskTracker.service;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task searchTask(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic searchEpic(int id) {
        return epics.get(id);
    }

    public void removeEpic(int id) {
        clearEpicSubTasks(id);
        epics.remove(id);
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    private void checkStatus(int id) {
        ArrayList<Integer> subtaskId = new ArrayList<>(epics.get(id).getSubTasksList());
        ArrayList<TaskStatus> status = new ArrayList<>();
        for (Integer subTaskId : subTasks.keySet()) {
            if (subtaskId.contains(subTaskId)) {
                status.add(subTasks.get(subTaskId).getStatus());
            }
        }
        ArrayList<TaskStatus> statusNew = new ArrayList<>();
        ArrayList<TaskStatus> statusInProgress = new ArrayList<>();
        ArrayList<TaskStatus> statusDone = new ArrayList<>();
        for (int i = 0; i < status.size(); i++) {
            if (status.get(i).equals(TaskStatus.DONE)) {
                statusDone.add(TaskStatus.DONE);
            } else if (status.get(i).equals(TaskStatus.IN_PROGRESS)) {
                statusInProgress.add(TaskStatus.IN_PROGRESS);
            } else if (status.get(i).equals(TaskStatus.NEW)) {
                statusNew.add(TaskStatus.NEW);
            }
        }
        if (!statusInProgress.isEmpty() && !statusNew.isEmpty() && !statusDone.isEmpty()
                || (!statusInProgress.isEmpty() && !statusNew.isEmpty())
                || (statusInProgress.isEmpty() && !statusNew.isEmpty() && !statusDone.isEmpty())
                || (!statusInProgress.isEmpty() && !statusDone.isEmpty())) {
            epics.get(id).setStatus(TaskStatus.IN_PROGRESS);
        } else if (!statusDone.isEmpty()) {
            epics.get(id).setStatus(TaskStatus.DONE);
        } else {
            epics.get(id).setStatus(TaskStatus.NEW);
        }
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epics.get(subTask.getEpicId()).addSubtaskId(subTask.getId());
        checkStatus(subTask.getEpicId());
    }

    public ArrayList<SubTask> getAllSubTasksEpic(int id) {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                subTasksList.add(subTask);
            }
        }
        return subTasksList;
    }

    public SubTask searchSubTask(int id) {
        return subTasks.get(id);
    }

    public void removeSubTask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        subTasks.remove(id);
        epics.get(epicId).removeSubTaskId(id);
        checkStatus(epicId);
    }

    public void clearEpicSubTasks(int id) {
        ArrayList<Integer> idSubTasks = new ArrayList<>(epics.get(id).getSubTasksList());
        for (Integer subTaskId:idSubTasks) {
            subTasks.remove(subTaskId);
            epics.get(id).clearSubTaskId();
        }
        checkStatus(id);
    }

    public void updateSabTask(SubTask subTask) {
       int epicId = subTask.getEpicId();
       int subtaskId = subTask.getId();
       subTasks.remove(subtaskId);
       subTasks.put(subtaskId, subTask);
       checkStatus(epicId);
    }
}