package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.SubTask;
import com.yandex.tracker.model.TaskStatus;
import server.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        String[] body;
        try (InputStream inputStream = httpExchange.getRequestBody()) {
            body = new String(inputStream.readAllBytes()).split("\n");
        }
        String messageForNotFoundId = "Эпик с ID %d не найден";
        switch (endpoint) {
            case GET_EPICS -> {
                String response = gson.toJson(HttpTaskServer.getTaskManager().getAllEpics());
                sendText(httpExchange, response, 200);
            }
            case GET_EPIC_BY_ID -> {
                int id = getTaskIdFromURI(httpExchange);
                Optional<Epic> epic = HttpTaskServer.getTaskManager().searchEpic(id);
                if (epic.isPresent()) {
                    String epicJson = gson.toJson(epic.get());
                    sendText(httpExchange, epicJson, 200);
                } else {
                    String response = String.format(messageForNotFoundId, id);
                    sendNotFound(httpExchange, response);
                }
            }
            case GET_EPIC_SUBTASKS -> {
                int id = getTaskIdFromURI(httpExchange);
                Optional<Epic> epicOptional = HttpTaskServer.getTaskManager().searchEpic(id);
                if (epicOptional.isEmpty()) {
                    String response = String.format(messageForNotFoundId, id);
                    sendNotFound(httpExchange, response);
                }
                List<SubTask> subtaskList = HttpTaskServer.getTaskManager().getAllSubTasksEpic(id);
                if (!subtaskList.isEmpty()) {
                    sendText(httpExchange, gson.toJson(subtaskList), 200);
                } else {
                    sendNotFound(httpExchange, "у эпика нет подзадач");
                }
            }
            case POST_EPICS -> {
                Epic epic = parseEpic(body);
                HttpTaskServer.getTaskManager().addEpic(epic);
                sendText(httpExchange, gson.toJson(epic), 201);
            }
            case DELETE_EPIC -> {
                int id = getTaskIdFromURI(httpExchange);
                if (HttpTaskServer.getTaskManager().getSetId().contains(id)) {
                    HttpTaskServer.getTaskManager().removeEpic(id);
                    sendText(httpExchange, "Эпик удален", 200);
                } else {
                    String response = String.format(messageForNotFoundId, id);
                    sendNotFound(httpExchange, response);
                }
            }
            default -> sendNotFound(httpExchange, "Неизвестный эндпоинт");
        }
    }

    private Epic parseEpic(String[] body) {
        StringBuilder stringBuilder = new StringBuilder(body[0]);
        for (int i = 1; i < body.length; i++) {
            stringBuilder.append(body[i]);
        }
        Epic epic = gson.fromJson(new String(stringBuilder), Epic.class);
        epic.setStatus(TaskStatus.NEW);
        epic.setEndTime(LocalDateTime.MAX);
        epic.setStartTime(LocalDateTime.MIN);
        epic.setDuration(Duration.ofHours(0));
        return epic;
    }
}