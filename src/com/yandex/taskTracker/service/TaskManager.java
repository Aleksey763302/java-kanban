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
    public void clearAllEpics() {
        epics.clear();
        subTasks.clear();
    }
    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epics.get(subTask.getEpicId()).addSubtaskId(subTask.getId());
        checkStatus(subTask.getEpicId());
    }
    public void clearAllSubtasks(){
        subTasks.clear();
        for (Epic epic:epics.values()) {
            epic.clearSubTaskId();
            checkStatus(epic.getId());
        }
    }
    public ArrayList<SubTask> getAllSubTasksEpic(int id) {
        ArrayList<Integer> subtasksId = new ArrayList<>(epics.get(id).getSubTasksList());
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer subTaskId : subtasksId) {
            subTasksList.add(subTasks.get(subTaskId));
        }
        return subTasksList;
    }
    public SubTask searchSubTask(int id) {
        return subTasks.get(id);
    }
    public void removeSubTask(int id) {
        int epicId = subTasks.remove(id).getEpicId();
        epics.get(epicId).removeSubTaskId(id);
        checkStatus(epicId);
    }
    public void updateSabTask(SubTask subTask) {
       int epicId = subTask.getEpicId();
       int subtaskId = subTask.getId();
       subTasks.put(subtaskId, subTask);
       checkStatus(epicId);
    }
    private void clearEpicSubTasks(int id) {
        ArrayList<Integer> idSubTasks = new ArrayList<>(epics.get(id).getSubTasksList());
        for (Integer subTaskId:idSubTasks) {
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
        if(countDone > 0 && countNew == 0){
            epics.get(id).setStatus(DONE);
        } else if (countDone != 0 && countNew != 0) {
            epics.get(id).setStatus(IN_PROGRESS);
        } else {
            epics.get(id).setStatus(NEW);
        }
    }
}