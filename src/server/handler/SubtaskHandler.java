package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.tracker.model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_SUBTASKS -> {
                String response = gson.toJson(taskManager.getAllSubTasks());
                sendText(httpExchange, response, 200);
            }
            case POST_SUBTASKS -> postSubtask(httpExchange);
            case GET_SUBTASK_BY_ID -> {
                int id = getTaskIdFromURI(httpExchange);
                Optional<SubTask> subTaskOptional = taskManager.searchSubTask(id);
                if (subTaskOptional.isPresent()) {
                    String response = gson.toJson(subTaskOptional.get());
                    sendText(httpExchange, response, 200);
                } else {
                    sendNotFound(httpExchange, "подзадача с ID: " + id + "не найдена");
                }
            }
            case DELETE_SUBTASK -> {
                int id = getTaskIdFromURI(httpExchange);
                if (taskManager.getSetId().contains(id)) {
                    taskManager.removeSubTask(id);
                    sendText(httpExchange, "подзадача удалена", 200);
                }
            }
            default -> sendNotFound(httpExchange, "неизвестный эндпоинт");
        }
    }

    private void postSubtask(HttpExchange httpExchange) throws IOException {
        String[] body;
        try (InputStream inputStream = httpExchange.getRequestBody()) {
            body = new String(inputStream.readAllBytes()).split("\n");
        }
        SubTask subTask = parseSubtask(body);
        if (!subTask.getName().contains("notFoundId")) {
            if (body.length == 9) {
                taskManager.updateSabTask(subTask);
                sendText(httpExchange, gson.toJson(subTask), 201);
            } else {
                if (taskManager.checkTime(subTask)) {
                    taskManager.addSubTask(subTask);
                    sendText(httpExchange, gson.toJson(subTask), 201);
                } else {
                    sendHasInteractions(httpExchange);
                }
            }
        } else if (subTask.getEpicId() == 809784) {
            sendNotFound(httpExchange, "в запросе не указан ID");
        } else {
            sendNotFound(httpExchange, "эпик с ID " + subTask.getEpicId() + " не найден");
        }
    }

    private SubTask parseSubtask(String[] body) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer epicID = null;
        for (String s : body) {
            stringBuilder.append(s);
            if (s.startsWith(" \"epicId") || s.startsWith("\"epicId")) {
                int index = s.lastIndexOf(":");
                epicID = Integer.parseInt(s.substring(index + 1).replace(" ", ""));
            }
        }
        if (epicID == null || !taskManager.getSetId().contains(epicID)) {
            int notFoundId = 809784;
            if (epicID != null) {
                notFoundId = epicID;
            }
            return new SubTask("notFoundId", "", notFoundId, LocalDateTime.now(), Duration.ofHours(0));
        }
        return gson.fromJson(new String(stringBuilder), SubTask.class);
    }
}
