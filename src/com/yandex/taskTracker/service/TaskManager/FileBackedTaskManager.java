package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.exceptions.ManagerSaveException;
import com.yandex.taskTracker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public Path saveFile;

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

    private void loadTasks() {
        String[] loadTasks;
        try {
            loadTasks = Files.readString(saveFile).split("}");
            if (!(loadTasks[loadTasks.length - 1].contains(","))) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String st : loadTasks) {
            Task task = fromString(st);
            String type = task.toString().substring(0, task.toString().indexOf("{")).toUpperCase();
            if (TasksType.valueOf(type) == TasksType.TASK) {
                tasks.put(task.getId(), task);
            } else if (TasksType.valueOf(type) == TasksType.EPIC) {
                epics.put(task.getId(), (Epic) task);
            } else if (TasksType.valueOf(type) == TasksType.SUBTASK) {
                subTasks.put(task.getId(), (SubTask) task);
                epics.get(((SubTask) task).getEpicId()).addSubtaskId(task.getId());
            }
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(saveFile.toString())) {
            if (saveFile == null) {
                throw new ManagerSaveException("файл для записи отсутствует");
            }
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
        sb.append(type);
        sb.append(",");
        sb.append(task.getName());
        sb.append(",");
        sb.append(task.getDescription());
        sb.append(",");
        sb.append(task.getStatus());
        sb.append(",");
        sb.append(task.getId());
        sb.append(",");
        if (type == TasksType.SUBTASK) {
            SubTask subtask = (SubTask) task;
            sb.append(subtask.getEpicId());
            sb.append(",");
        }
        sb.append(task.getDuration());
        sb.append(",");
        sb.append(task.getEndTime().minus(task.getDuration()));
        sb.append("}");
        return sb.toString();
    }

    private Task fromString(String value) {
        Task task;
        String[] taskFields = value.split(",");
        TasksType taskType;
        Duration duration;
        LocalDateTime dateTime;
        if (!(taskFields.length < 3)) {
            taskType = TasksType.valueOf(taskFields[0]);
            if (taskType == TasksType.SUBTASK) {
                duration = Duration.parse(taskFields[6]);
                dateTime = LocalDateTime.parse(taskFields[7]);
            } else {
                duration = Duration.parse(taskFields[5]);
                dateTime = LocalDateTime.parse(taskFields[6]);
            }
        } else {
            taskType = null;
            duration = null;
            dateTime = null;
        }
        if (taskType == TasksType.TASK) {
            task = new Task(taskFields[1], taskFields[2], dateTime, duration);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
        } else if (taskType == TasksType.EPIC) {
            task = new Epic(taskFields[1], taskFields[2], dateTime, duration);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
        } else if (taskType == TasksType.SUBTASK) {
            task = new SubTask(taskFields[1], taskFields[2], Integer.parseInt(taskFields[5]), dateTime, duration);
            task.setId(Integer.parseInt(taskFields[4]));
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
        } else {
            task = null;
        }

        return task;
    }
}