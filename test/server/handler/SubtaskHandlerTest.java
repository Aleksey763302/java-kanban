package server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.SubTask;
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

import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void beforeEach() {
        taskManager.clearAllSubtasks();
        server.serverStart();
    }

    @AfterEach
    public void afterEach() {
        server.serverClose();
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        taskManager.addSubTask(subTask);

        HttpResponse<String> response;
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        taskManager.addSubTask(subTask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        uri = URI.create("http://localhost:8080/subtasks/999");
        request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void postSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        String subtaskJson = gson.toJson(subTask);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        SubTask subTask2 = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T20:13",
                2);
        subtaskJson = gson.toJson(subTask2);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        taskManager.addSubTask(subTask);

        SubTask subTaskUpdate = new SubTask("subtask", "description", epic.getId(), subTask.getId(),
                "2024-01-01T19:13",
                2);
        String subTaskUpdateJson = gson.toJson(subTaskUpdate);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(subTaskUpdateJson)).uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "descr");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T19:13",
                2);
        SubTask subTask2 = new SubTask("subtask", "description", epic.getId(),
                "2024-01-01T23:13",
                2);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask2);
        final int id = subTask2.getId();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}