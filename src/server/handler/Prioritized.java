package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.HttpTaskServer;

import java.io.IOException;

public class Prioritized extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint.equals(Endpoint.GET_PRIORITIZED)) {
            String response = gson.toJson(HttpTaskServer.getTaskManager().getPrioritizedTasks());
            sendText(exchange, response, 200);
        } else {
            sendNotFound(exchange, "неизвестный эндпоинт");
        }
    }
}