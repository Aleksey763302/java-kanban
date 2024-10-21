package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        String[] body;
        try (InputStream inputStream = httpExchange.getRequestBody()) {
            body = new String(inputStream.readAllBytes()).split("\n");
        }
        switch (endpoint) {
            case GET_TASKS -> {
                String response = gson.toJson(taskManager.getAllTasks());
                sendText(httpExchange, response, 200);
            }
            case GET_TASK_BY_ID -> {
                int id = getTaskIdFromURI(httpExchange);
                Optional<Task> taskOpt = taskManager.searchTask(id);
                if (taskOpt.isPresent()) {
                    String response = gson.toJson(taskOpt.get());
                    sendText(httpExchange, response, 200);
                } else {
                    sendNotFound(httpExchange, "задача с ID " + id + " не найдена");
                }
            }
            case POST_TASKS -> {
                Task task = parseTask(body);
                if (body.length == 4) {
                    if (taskManager.checkTime(task)) {
                        taskManager.addTask(task);
                        sendText(httpExchange, gson.toJson(task), 201);
                    } else {
                        sendHasInteractions(httpExchange);
                    }
                } else if (body.length == 6) {
                    taskManager.updateTask(task);
                    sendText(httpExchange, gson.toJson(task), 201);
                }
            }
            case DELETE_TASK -> {
                int id = getTaskIdFromURI(httpExchange);
                if (taskManager.getSetId().contains(id)) {
                    taskManager.removeTask(id);
                    sendText(httpExchange, "Задача удалена", 200);
                } else {
                    sendNotFound(httpExchange, "Задача с ID: " + id + " не найдена");
                }
            }
            default -> sendNotFound(httpExchange, "Неизвестный эндпоинт");
        }
    }

    private Task parseTask(String[] body) {
        StringBuilder stringBuilder = new StringBuilder(body[0]);
        boolean checkID = false;
        for (int i = 1; i < body.length; i++) {
            stringBuilder.append(body[i]);
            if (body[i].startsWith("\"id")) {
                checkID = true;
            }
        }
        if (checkID) {
            return gson.fromJson(new String(stringBuilder), Task.class);
        }
        Task task = gson.fromJson(new String(stringBuilder), Task.class);
        task.setId(task.hashCode());
        task.setStatus(TaskStatus.NEW);
        return task;
    }

}