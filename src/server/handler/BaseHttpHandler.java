package server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.manager.TaskManager;
import server.handler.adapters.DurationAdapter;
import server.handler.adapters.LocalDataAdapter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    TaskManager taskManager;
    protected Gson gson;
    static final String GET = "GET";
    static final String POST = "POST";
    static final String DELETE = "DELETE";
    static final String TASKS = "tasks";
    static final String EPICS = "epics";
    static final String SUBTASKS = "subtasks";

    public BaseHttpHandler() {
        this.taskManager = Managers.loadFromFile(new File("resources\\saveFile.CSV"));
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    protected void sendText(HttpExchange httpExchange, String response, int rCode) throws IOException {
        httpExchange.sendResponseHeaders(rCode, 0);
        writeResponse(httpExchange, response);
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        writeResponse(httpExchange, response);
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(406, 0);
        writeResponse(httpExchange, "Обнаружено пересечение с другой задачей");
    }

    private void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(response.getBytes(Charset.defaultCharset()));
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (!getEndpointTask(path, requestMethod).equals(Endpoint.UNKNOWN)) {
            return getEndpointTask(path, requestMethod);
        }
        if (!getEndpointEpic(path, requestMethod).equals(Endpoint.UNKNOWN)) {
            return getEndpointEpic(path, requestMethod);
        }
        if (!getEndpointSubtask(path, requestMethod).equals(Endpoint.UNKNOWN)) {
            return getEndpointSubtask(path, requestMethod);
        }
        if (path[1].equals("prioritized") && requestMethod.equals("GET")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (path[1].equals("history") && requestMethod.equals("GET")) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.UNKNOWN;
    }

    protected int getTaskIdFromURI(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = -1;
        try {
            id = Integer.parseInt(path[2]);
        } catch (NumberFormatException exception) {
            sendNotFound(httpExchange, "некорректный ID задачи");
        }
        return id;
    }

    private Endpoint getEndpointTask(String[] path, String requestMethod) {
        if (path.length == 2 && path[1].equals(TASKS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_TASKS;
                case POST -> Endpoint.POST_TASKS;
                default -> Endpoint.UNKNOWN;
            };
        }
        if (path.length == 3 && path[1].equals(TASKS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_TASK_BY_ID;
                case DELETE -> Endpoint.DELETE_TASK;
                default -> Endpoint.UNKNOWN;
            };
        }
        return Endpoint.UNKNOWN;
    }

    private Endpoint getEndpointEpic(String[] path, String requestMethod) {
        if (path.length == 2 && path[1].equals(EPICS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_EPICS;
                case POST -> Endpoint.POST_EPICS;
                default -> Endpoint.UNKNOWN;
            };
        }
        if (path.length == 3 && path[1].equals(EPICS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_EPIC_BY_ID;
                case DELETE -> Endpoint.DELETE_EPIC;
                default -> Endpoint.UNKNOWN;
            };
        }
        if (path.length == 4 && path[1].equals(EPICS) && path[3].equals(SUBTASKS)) {
            if (requestMethod.equals(GET)) {
                return Endpoint.GET_EPIC_SUBTASKS;
            } else {
                return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private Endpoint getEndpointSubtask(String[] path, String requestMethod) {
        if (path.length == 2 && path[1].equals(SUBTASKS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_SUBTASKS;
                case POST -> Endpoint.POST_SUBTASKS;
                default -> Endpoint.UNKNOWN;
            };
        }
        if (path.length == 3 && path[1].equals(SUBTASKS)) {
            return switch (requestMethod) {
                case GET -> Endpoint.GET_SUBTASK_BY_ID;
                case DELETE -> Endpoint.DELETE_SUBTASK;
                default -> Endpoint.UNKNOWN;
            };
        }
        return Endpoint.UNKNOWN;
    }
}