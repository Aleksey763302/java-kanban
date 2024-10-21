package com.yandex.tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.SubTask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskStatus;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.manager.TaskManager;
import server.handler.adapters.DurationAdapter;
import server.handler.adapters.LocalDataAdapter;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = Managers.loadFromFile(new File("resources\\saveFile.CSV"));
        System.out.println("Поехали!");
      //   tm.clearTasks();
      //   tm.clearAllEpics();
      //   addTasks(tm);
        // addEpics(tm);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
        tm.removeEpic(22);


       // Task task = gson.fromJson(str,Task.class);



        //Task task = gson.fromJson();

        Epic epic = new Epic("epic", "descr");
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        String str = gson.toJson(epic);
        String str2 = gson.toJson(subTask);

        System.out.println(gson.fromJson(str2, SubTask.class));



        printAllTasks(tm);
    }

    private static void addTasks(TaskManager tm) {

        Task task1 = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        tm.addTask(task1);

        Task task2 = new Task("task 2", "description",
                "2024-01-01T12:13", 2);
        tm.addTask(task2);
        Task task3 = new Task("task3", "description",
                "2024-01-01T22:13", 2);
        tm.addTask(task3);
        Task task4 = new Task("task4", "description",
                "2024-01-01T13:13", 2);
        tm.addTask(task4);
        Task task5 = new Task("task5", "description",
                "2024-01-01T15:13", 2);
        tm.addTask(task5);
        Task task6 = new Task("task6", "description",
                "2024-01-01T17:13", 2);
        tm.addTask(task6);
        Task task7 = new Task("task7", "description",
                "2024-01-01T12:14", 2);
        tm.addTask(task7);
        Task task8 = new Task("task8", "description",
                "2024-01-01T08:13", 2);
        tm.addTask(task8);
        Task task9 = new Task("task9", "description",
                "2024-01-01T09:13", 3);
        tm.addTask(task9);
    }

    private static void addEpics(TaskManager tm) {
        Epic epic = new Epic("epic", "descr");
        tm.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        tm.addSubTask(subTask);
        SubTask subTask2 = new SubTask("subtask2", "description2", epic.getId(),
                "2024-02-01T14:13",
                3);
        tm.addSubTask(subTask2);

        Epic epic2 = new Epic("epic2", "descr");
        tm.addEpic(epic2);
        SubTask subTaskE2 = new SubTask("subtaskE2", "description", epic2.getId(),
                "2024-03-01T18:13",
                2);
        tm.addSubTask(subTaskE2);
        SubTask subTaskE3 = new SubTask("subtaskE3", "description", epic2.getId(),
                "2024-04-01T11:13",
                2);
        tm.addSubTask(subTaskE3);

        SubTask subTaskUp = new SubTask("subtaskE3", "description2", epic2.getId(), subTaskE3.getId(),
                "2024-03-01T13:13",
                3);
        subTaskUp.setStatus(TaskStatus.DONE);
        tm.updateSabTask(subTaskUp);
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