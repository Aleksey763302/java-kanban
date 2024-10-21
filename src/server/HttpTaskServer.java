package server;

import com.sun.net.httpserver.HttpServer;
import server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/epics", new EpicHandler());
        server.createContext("/subtasks", new SubtaskHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new Prioritized());
        System.out.println("сервер запущен на порту :" + PORT);
        server.start();
    }
}
