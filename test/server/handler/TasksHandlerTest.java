package server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskStatus;
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

class TasksHandlerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDataAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void beforeEach() {
        taskManager.clearTasks();
        server.serverStart();
    }

    @AfterEach
    public void afterEach() {
        server.serverClose();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        taskManager.addTask(task);

        HttpResponse<String> response;
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        taskManager.addTask(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        uri = URI.create("http://localhost:8080/tasks/999");
        request = HttpRequest.newBuilder().GET().uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

    }

    @Test
    void postTask() throws IOException, InterruptedException {
        Task task = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        String taskJson = gson.toJson(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        Optional<Task> taskOptional = taskManager.searchTask(0);
        String name = null;
        if (taskOptional.isPresent()) {
            name = taskOptional.get().getName();
        }
        assertNotNull(name, "Задача не возвращается");
        assertEquals("task 1", name, "Некорректное имя задачи");

        Task task2 = new Task("task 1", "destruction", "2024-01-01T13:13", 2);
        taskJson = gson.toJson(task2);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task task = new Task("task 1", "destruction", "2024-01-01T12:13", 2);
        taskManager.addTask(task);
        Task taskUpdate = new Task("task update", "destruction",
                TaskStatus.DONE, LocalDateTime.parse("2024-01-01T12:13"), Duration.ofHours(2), 0);

        String taskJson = gson.toJson(taskUpdate);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(uri).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        String name = null;
        Optional<Task> taskOptional = taskManager.searchTask(0);
        if (taskOptional.isPresent()) {
            name = taskOptional.get().getName();
        }
        assertNotNull(name, "задача не возвращается");
        assertEquals("task update", name, "Некорректное имя задачи");
    }
}