package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint.equals(Endpoint.GET_HISTORY)) {
            String response = gson.toJson(taskManager.getHistory());
            sendText(exchange, response, 200);
        } else {
            sendNotFound(exchange, "неизвестный эндпоинт");
        }
    }
}
