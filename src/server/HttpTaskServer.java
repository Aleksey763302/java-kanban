package server;

import com.sun.net.httpserver.HttpServer;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.manager.TaskManager;
import server.handler.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static TaskManager taskManager;

    private static final int PORT = 8080;

    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new Prioritized());
    }

    public static void main(String[] args) {

    }

    public void serverStart() {
        server.start();
    }

    public void serverClose() {
        server.stop(1);
    }

    public static TaskManager getTaskManager() {
        return taskManager;
    }
}
