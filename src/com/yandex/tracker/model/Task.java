package com.yandex.tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private TaskStatus status;
    private Integer id;
    Duration duration;
    LocalDateTime startTime;

    public Task(String name, String description,
                String time, int duration) {
        this.name = name;
        this.description = description;
        this.status = null;
        this.id = null;
        this.startTime = LocalDateTime.parse(time);
        this.duration = Duration.ofHours(duration);
    }

    public Task(String name, String description,
                LocalDateTime dateTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.id = hashCode();
        this.startTime = dateTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskStatus status,
                LocalDateTime dateTime, Duration duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = dateTime;
        this.duration = duration;
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}