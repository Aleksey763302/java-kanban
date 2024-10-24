package server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.handler.adapters.DurationAdapter;
import server.handler.adapters.LocalDataAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void beforeEach() {
        taskManager.clearAllEpics();
        server.serverStart();
    }

    @AfterEach
    public void afterEach() {
        server.serverClose();
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        HttpResponse<String> response;
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        uri = URI.create("http://localhost:8080/epics/999");
        request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void postEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        String epicJson = gson.toJson(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Optional<Epic> epicOptional = taskManager.searchEpic(0);
        String name = null;
        if (epicOptional.isPresent()) {
            name = epicOptional.get().getName();
        }
        assertNotNull(name, "эпик не возвращяется");
        assertEquals("epic", name, "некорректное имя");
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}