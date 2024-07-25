package com.yandex.taskTracker;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager tm = new TaskManager();
        Task task1 = new Task("Task 1","description task");
        Task task2 = new Task("Task 2","description task");

        Epic epic1 = new Epic("Epic 1","description");
        Epic epic2 = new Epic("Epic 2","description");

        SubTask subTask1 = new SubTask("Sub 1","description", epic1.getId());
        SubTask subTask2 = new SubTask("Sub 2","description", epic1.getId());
        SubTask subTask3 = new SubTask("Sub 3","description", epic2.getId());

        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addEpic(epic2);

        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        tm.addSubTask(subTask3);

        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubTasksEpic(epic1.getId()));

        task1.setStatus(TaskStatus.DONE);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.DONE);
        subTask3.setStatus(TaskStatus.DONE);

        tm.updateSabTask(subTask1);
        tm.updateSabTask(subTask2);
        tm.updateSabTask(subTask3);
        tm.removeSubTask(subTask1.getId());

        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubTasksEpic(epic1.getId()));
    }
}