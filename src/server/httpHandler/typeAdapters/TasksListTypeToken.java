package server.httpHandler.typeAdapters;

import com.google.gson.reflect.TypeToken;
import com.yandex.taskTracker.model.Task;

import java.util.List;

public class TasksListTypeToken extends TypeToken<List<Task>> {
}
