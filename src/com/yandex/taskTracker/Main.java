package com.yandex.taskTracker;

import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.TaskManager.TaskManager;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File savefile = new File("resources\\save.txt");
        TaskManager tm = Managers.loadFromFile(savefile);
        System.out.println("Поехали!");
        System.out.println(tm.searchEpic(3));
        System.out.println(tm.searchEpic(4));
        System.out.println(tm.searchEpic(3));
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