package TaskManager.Type;

import java.util.Objects;

public class Epic {


    private String name;
    private String description;
    private TaskStatus status;
    private int id;

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return id == epic.id
                && Objects.equals(name, epic.name)
                && Objects.equals(description, epic.description)
                && status == epic.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

}





