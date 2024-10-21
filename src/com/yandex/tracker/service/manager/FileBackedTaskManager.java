package com.yandex.tracker.service.manager;

import com.yandex.tracker.exceptions.ManagerSaveException;
import com.yandex.tracker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path saveFile;

    public FileBackedTaskManager(File file) {
        saveFile = file.toPath();
        loadTasks();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        loadTasks();
        return super.getAllTasks();
    }

    @Override
    public List<SubTask> getAllSubTasksEpic(int id) {
        loadTasks();
        return super.getAllSubTasksEpic(id);
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        loadTasks();
        return super.getAllSubTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        loadTasks();
        return super.getAllEpics();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSabTask(SubTask subTask) {
        super.updateSabTask(subTask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    private void loadTasks() {
        String[] loadTasks;
        try {
            loadTasks = Files.readString(saveFile).split("\n");
            if (loadTasks.length == 1) {
                return;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("файл не найден");
        }
        for (int i = 1; i < loadTasks.length; i++) {
            Optional<Task> taskOptional = fromString(loadTasks[i]);
            if (taskOptional.isEmpty()) {
                return;
            }
            Task task = taskOptional.get();
            String type = task.toString().substring(0, task.toString().indexOf("{")).toUpperCase();
            if (TasksType.valueOf(type) == TasksType.TASK) {
                tasks.put(task.getId(), task);
            } else if (TasksType.valueOf(type) == TasksType.EPIC) {
                epics.put(task.getId(), (Epic) task);
            } else if (TasksType.valueOf(type) == TasksType.SUBTASK) {
                subTasks.put(task.getId(), (SubTask) task);
                epics.get(((SubTask) task).getEpicId()).addSubtaskId(task.getId());
                addDataTimeAndDuration(task.getId());
            }
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(saveFile.toString())) {
            fileWriter.write("Type,Name,Description,Status,ID,EpicID,Duration,StartTime\n");
            List<Task> tasks = getAllTasks();
            List<Epic> epics = getAllEpics();
            List<SubTask> subTasks = getAllSubTasks();
            for (Task task : tasks) {
                fileWriter.write(toString(task));
            }
            for (Task task : epics) {
                fileWriter.write(toString(task));
            }
            for (Task task : subTasks) {
                fileWriter.write(toString(task));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        String[] split = task.toString().split("\\{");
        TasksType type = TasksType.valueOf(split[0].toUpperCase());
        sb.append(type).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getDescription()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getId()).append(",");
        if (type == TasksType.SUBTASK) {
            SubTask subtask = (SubTask) task;
            sb.append(subtask.getEpicId()).append(",");
        } else {
            sb.append(",");
        }
        sb.append(task.getDuration()).append(",");
        sb.append(task.getEndTime().minus(task.getDuration()));
        sb.append("\n");
        return sb.toString();
    }

    private Optional<TasksType> getType(String stringStatus) {
        try {
            return Optional.of(TasksType.valueOf(stringStatus));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private Optional<Task> fromString(String value) {
        String[] taskFields = value.split(",");
        Optional<TasksType> typeOptional = getType(taskFields[0]);
        if (typeOptional.isEmpty()) {
            return Optional.empty();
        }
        TasksType taskType = typeOptional.get();
        Duration duration;
        LocalDateTime dateTime;
        duration = Duration.parse(taskFields[6]);
        dateTime = LocalDateTime.parse(taskFields[7]);
        if (taskType == TasksType.TASK) {
            Task task = new Task(taskFields[1], taskFields[2], dateTime, duration);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
            return Optional.of(task);
        } else if (taskType == TasksType.EPIC) {
            Task task = new Epic(taskFields[1], taskFields[2]);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
            return Optional.of(task);
        } else if (taskType == TasksType.SUBTASK) {
            Task task = new SubTask(taskFields[1], taskFields[2], Integer.parseInt(taskFields[5]), dateTime, duration);
            task.setId(Integer.parseInt(taskFields[4]));
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            return Optional.of(task);
        }
        return Optional.empty();
    }
}