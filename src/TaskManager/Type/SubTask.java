package TaskManager.Type;

import java.util.Objects;

public class SubTask {

    private String name;
    private String description;
    private TaskStatus status;
    private int id;
    private int epicId;

    public SubTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.id = hashCode();
    }

    public SubTask() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return id == subTask.id && epicId == subTask.epicId && Objects.equals(name, subTask.name) && Objects.equals(description, subTask.description) && status == subTask.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, epicId);
    }
}