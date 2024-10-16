package server.httpHandler;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskTracker.model.Task;
import com.google.gson.Gson;
import com.yandex.taskTracker.model.TaskStatus;
import server.httpHandler.typeAdapters.DurationAdapter;
import server.httpHandler.typeAdapters.LocalDataAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        String[] body;
        try (InputStream inputStream = httpExchange.getRequestBody()) {
            body = new String(inputStream.readAllBytes()).split("\n");
        }
        switch (endpoint) {
            case GET_TASKS -> {
                String response = taskManager.getAllTasks().stream()
                        .map(task -> gson.toJson(task))
                        .collect(Collectors.joining("\n"));
                sendText(httpExchange, response, 200);
            }
            case GET_TASK_BY_ID -> {
                int id = getTaskID(httpExchange);
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
                        sendText(httpExchange, "новая задача добавлена", 201);
                    } else {
                        sendHasInteractions(httpExchange, "обнаружено пересечение с другой задачей");
                    }
                } else if (body.length == 6) {
                    taskManager.updateTask(task);
                    sendText(httpExchange, "задача обновлена", 201);
                }
            }
            case DELETE_TASK -> {
                int id = getTaskID(httpExchange);
                if(taskManager.getSetId().contains(id)){
                    taskManager.removeTask(id);
                    sendText(httpExchange,"задача удалена",200);
                }else{
                    sendNotFound(httpExchange,"задача с ID " + id + " не найдена");
                }
            }
            default -> sendNotFound(httpExchange, "неизвестный эндпоинт");
        }
    }

    private Task parseTask(String[] body) {
        LocalDateTime startTime = LocalDateTime.parse(body[1]);
        Duration duration = Duration.ofHours(Integer.parseInt(body[2]));
        if (body.length == 6) {
            Optional<TaskStatus> optionalTaskStatus = parseStatus(body[4]);
            if (optionalTaskStatus.isPresent()) {
                TaskStatus status = optionalTaskStatus.get();
                int id = Integer.parseInt(body[5]);
                return new Task(body[0], body[3], status, startTime, duration, id);
            }
        }
        return new Task(body[0], body[3], startTime, duration);
    }

}