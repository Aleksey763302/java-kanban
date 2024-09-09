package com.yandex.taskTracker;

import com.yandex.taskTracker.model.Epic;
import com.yandex.taskTracker.model.SubTask;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager tm =  Managers.getDefault();
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
        tm.searchTask(task2.getId());
        tm.searchEpic(epic1.getId());
        tm.searchSubTask(subTask1.getId());
        tm.searchSubTask(subTask2.getId());
        tm.searchSubTask(subTask1.getId());
        tm.removeTask(task2.getId());
        tm.removeSubTask(subTask2.getId());
        System.out.println("Поехали!");
        printAllTasks(tm);
    }
    
    private static void printAllTasks(TaskManager tm) {
        System.out.println("Задачи:");
        for (Task task : tm.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : tm.getAllEpics()) {
            System.out.println(epic);
            for (Task task : tm.getAllSubTasksEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : tm.getAllSubTasks()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : tm.getHistory()) {
            System.out.println(task);
        }
    }
}
