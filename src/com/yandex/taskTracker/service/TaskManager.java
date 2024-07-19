package com.yandex.taskTracker.service;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, ArrayList<SubTask>> subTasks = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task searchTask(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTaskList() {
        tasks.clear();
    }

    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }

    public void updateTask(Task oldTask, Task updateTask) {
        for (Integer task : tasks.keySet()) {
            if (task == oldTask.getId()) {
                tasks.put(oldTask.getId(), updateTask);
            }
        }
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        subTasks.put(epic.getId(), new ArrayList<>());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic searchEpic(int id) {
        checkStatus(epics.get(id));
        return epics.get(id);
    }

    public void removeEpic(Epic epic) {
        epics.remove(epic.getId());
        subTasks.remove(epic.getId());
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    private void checkStatus(Epic epic) {
        ArrayList<TaskStatus> status = new ArrayList<>();
        for (Integer id : subTasks.keySet()) {
            if (id == epic.getId()) {
                for (int i = 0; i < subTasks.get(epic.getId()).size(); i++) {
                    status.add(subTasks.get(epic.getId()).get(i).getStatus());
                }
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
        if (!statusInProgress.isEmpty() && !statusNew.isEmpty() || (statusInProgress.isEmpty() && !statusNew.isEmpty())) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (!statusDone.isEmpty() && statusInProgress.isEmpty() && statusNew.isEmpty()) {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    public void addSubTaskToEpic(Epic epic, SubTask subTask) {
        for (Integer id : subTasks.keySet()) {
            if (id == epic.getId()) {
                subTask.setEpicId(epic.getId());
                subTasks.get(epic.getId()).add(subTask);
            }
        }
    }

    public ArrayList<String> getAllSubTasksEpic(Epic epic) {
        ArrayList<String> subTasksList = new ArrayList<>();
        for (Integer idEpic : subTasks.keySet()) {
            if (idEpic == epic.getId()) {
                for (int i = 0; i < subTasks.get(epic.getId()).size(); i++) {
                    subTasksList.add(subTasks.get(epic.getId()).get(i).getName());
                }
            }
        }
        return subTasksList;
    }

    public SubTask searchSubTaskID(int id) {
        SubTask sub = new SubTask();
        for (ArrayList<SubTask> subtasks: subTasks.values()) {
            for (SubTask subTask: subtasks) {
                if (subTask.getId() == id){
                   sub = subTask;
                }
            }
        }
        return sub;
    }

    public void removeSubTask(SubTask subTask) {
        for (ArrayList<SubTask> sub : subTasks.values()) {
            sub.remove(subTask);
            if (sub.isEmpty()){
                epics.remove(subTask.getEpicId());
            }
        }
    }

    public void clearSubTask(Epic epic) {
        subTasks.put(epic.getId(), new ArrayList<>());
    }

    public void updateSab(SubTask oldSub, SubTask newSub) {
        SubTask subTaskNEW = new SubTask();
        for (ArrayList<SubTask> subList : subTasks.values()) {
            for (SubTask subTask : subList) {
                if (oldSub.getId() == subTask.getId()) {
                    subTaskNEW.setId(oldSub.getId());
                    subTaskNEW.setEpicId(oldSub.getEpicId());
                    subTaskNEW.setStatus(TaskStatus.DONE);
                    subTaskNEW.setName(newSub.getName());
                    subTaskNEW.setDescription(newSub.getDescription());
                }
            }
        }
        removeSubTask(oldSub);
        subTasks.get(subTaskNEW.getEpicId()).add(subTaskNEW);
    }
}