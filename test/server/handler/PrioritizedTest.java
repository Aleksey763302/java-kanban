package server.handler;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.manager.TaskManager;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);

    @Test
    void getPrioritizedList() throws IOException, InterruptedException {
        server.serverStart();
        Task task1 = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        taskManager.addTask(task1);
        Task task2 = new Task("task 2", "description",
                "2024-01-01T12:13", 2);
        taskManager.addTask(task2);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        server.serverClose();
    }
}