package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.HistoryManager.HistoryManager;
import com.yandex.taskTracker.service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> tasks = new TreeSet<>((Task task1, Task task2) -> {
            if (task1.getEndTime().minus(task1.getDuration()).isBefore(task2.getEndTime().minus(task2.getDuration()))) {
                return 1;
            } else {
                return -1;
            }
        });
        tasks.addAll(getAllTasks());
        tasks.addAll(getAllEpics());
        tasks.addAll(getAllSubTasks());
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public int giveID() {
        int id = tasks.size() + epics.size() + subTasks.size();
        if (tasks.containsKey(id) || epics.containsKey(id) || subTasks.containsKey(id)) {
            id *= 2;
        }
        return id;
    }

    @Override
    public void addTask(Task task) {
        task.setId(giveID());
        if (checkTime(task)) {
            tasks.put(task.getId(), task);
        }
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
        epic.setId(giveID());
        if (checkTime(epic)) {
            epics.put(epic.getId(), epic);
        }
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
        subTask.setId(giveID());
        if (checkTime(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            int epicId = subTask.getEpicId();
            epics.get(epicId).addSubtaskId(subTask.getId());
            checkStatus(epicId);
            addDataTimeAndDuration(subTask.getId());
        }
    }

    @Override
    public void clearAllSubtasks() {
        subTasks.clear();
        epics.values().forEach(epic -> {
            epic.clearSubTaskId();
            checkStatus(epic.getId());
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(LocalDateTime.MIN);
            epic.setEndTime(LocalDateTime.MIN);
        });
    }

    @Override
    public List<SubTask> getAllSubTasksEpic(int id) {
        ArrayList<Integer> subtasksId = new ArrayList<>(epics.get(id).getSubTasksList());
        return subtasksId.stream()
                .map(subTasks::get)
                .collect(Collectors.toList());
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

    protected void addDataTimeAndDuration(int id) {
        SubTask subTask = subTasks.get(id);
        int epicId = subTask.getEpicId();
        List<Integer> subtasksEpic = epics.get(epicId).getSubTasksList();
        if (subtasksEpic.size() == 1) {
            epics.get(epicId).setDuration(subTask.getDuration());
            epics.get(epicId).setEndTime(subTask.getEndTime());
            epics.get(epicId).setStartTime(subTask.getEndTime().minus(subTask.getDuration()));
        } else {
            if (epics.get(epicId).getStartTime().isAfter(subTask.getEndTime().minus(subTask.getDuration()))) {
                epics.get(epicId).setStartTime(subTask.getEndTime().minus(subTask.getDuration()));
            }
            if (epics.get(epicId).getEndTime().isBefore(subTask.getEndTime())) {
                epics.get(epicId).setEndTime(subTask.getEndTime());
            }
            Duration durationEpic = epics.get(epicId).getDuration();
            epics.get(epicId).setDuration(durationEpic.plus(subTask.getDuration()));
        }
    }

    private void clearEpicSubTasks(int id) {
        ArrayList<Integer> idSubTasks = new ArrayList<>(epics.get(id).getSubTasksList());
        idSubTasks.forEach(subTasks::remove);
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

    private boolean checkTime(Task task) {
        List<Task> taskList = new ArrayList<>(getPrioritizedTasks());
        List<Boolean> check = new ArrayList<>();
        if (taskList.isEmpty()) {
            return true;
        }
        taskList.forEach(task1 -> {
            LocalDateTime startTime = task1.getEndTime().minus(task1.getDuration());
            LocalDateTime endTime = task1.getEndTime();
            if (startTime.isBefore(task.getEndTime().minus(task.getDuration()))
                    && endTime.isAfter(task.getEndTime().minus(task.getDuration()))) {
                check.add(false);
            }
        });
        return check.isEmpty();
    }
}