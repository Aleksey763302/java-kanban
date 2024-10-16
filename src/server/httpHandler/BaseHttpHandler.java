package server.httpHandler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.TaskStatus;
import com.yandex.taskTracker.service.Managers;
import com.yandex.taskTracker.service.taskManager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

public class BaseHttpHandler {
    TaskManager taskManager;

    public BaseHttpHandler() {
        this.taskManager = Managers.loadFromFile(new File("resources\\saveFile.CSV"));
    }

    protected void sendText(HttpExchange httpExchange, String response, int rCode) throws IOException {
        httpExchange.sendResponseHeaders(rCode, 0);
        writeResponse(httpExchange, response);
    }

    protected void sendNotFound(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        writeResponse(httpExchange, response);
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(406, 0);
        writeResponse(httpExchange, response);
    }

    private void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response.getBytes(Charset.defaultCharset()));
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path.length == 2 && path[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASKS;
            }
            return Endpoint.UNKNOWN;
        }
        if (path.length == 3 && path[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASK_BY_ID;
            }
            if (requestMethod.equals("DELETE")){
                return Endpoint.DELETE_TASK;
            }
            return Endpoint.UNKNOWN;
        }
        if (path.length == 2 && path[1].equals("epics")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_EPICS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_EPICS;
            }
            return Endpoint.UNKNOWN;
        }
        if (path.length == 2 && path[1].equals("subtasks")) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_SUBTASKS;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_SUBTASKS;
            }
            return Endpoint.UNKNOWN;
        }
        return Endpoint.UNKNOWN;
    }

    protected int getTaskID(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = -1;
        try {
          id = Integer.parseInt(path[2]);
        } catch (NumberFormatException exception) {
            sendNotFound(httpExchange, "некорректный ID задачи");
        }
        return id;
    }

    protected Optional<TaskStatus> parseStatus(String statusString) {
        TaskStatus[] statusList = TaskStatus.values();
        if (Arrays.stream(statusList).map(TaskStatus::toString).toList().contains(statusString.toUpperCase())) {
            return Optional.of(TaskStatus.valueOf(statusString.toUpperCase()));
        } else {
            return Optional.empty();
        }
    }
}
