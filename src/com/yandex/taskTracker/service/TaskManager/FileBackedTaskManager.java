package com.yandex.taskTracker.service.TaskManager;

import com.yandex.taskTracker.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static Path saveFile;

    public FileBackedTaskManager() {
        try {
            loadTasks();
            Files.delete(saveFile);
            Files.createFile(Paths.get("save.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            }
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter("save.txt")) {
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
            throw new RuntimeException(e);
        }
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        String[] taskType = task.toString().split("\\{");
        sb.append(taskType[0].toUpperCase());
        sb.append(",");
        String[] taskFields = taskType[1].split(",");
        for (String field : taskFields) {
            int index = field.indexOf("=") + 1;
            sb.append(field.substring(index));
            sb.append(",");
            if (sb.indexOf("'") != -1) {
                sb.deleteCharAt(sb.indexOf("'"));
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        return new String(sb);
    }

    private Task fromString(String value) {
        Task task;
        String[] taskFields = value.split(",");
        TasksType taskType;
        if (!(taskFields.length < 3)) {
            taskType = TasksType.valueOf(taskFields[0]);
        } else {
            taskType = null;
        }
        if (taskType == TasksType.TASK) {
            task = new Task(taskFields[1], taskFields[2]);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
        } else if (taskType == TasksType.EPIC) {
            task = new Epic(taskFields[1], taskFields[2]);
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
            task.setId(Integer.parseInt(taskFields[4]));
        } else if (taskType == TasksType.SUBTASK) {
            task = new SubTask(taskFields[1], taskFields[2], Integer.parseInt(taskFields[5]));
            task.setId(Integer.parseInt(taskFields[4]));
            task.setStatus(TaskStatus.valueOf(taskFields[3]));
        } else {
            task = null;
        }

        return task;
    }
}