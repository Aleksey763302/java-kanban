package com.yandex.taskTracker.service.historyManager;

import com.yandex.taskTracker.model.Task;
import com.yandex.taskTracker.model.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();
    private final HashMap<Integer, Node<Task>> idTasks = new HashMap<>();

    public Node<Task> tail;
    public Node<Task> head;

    public void remove(int id) {
        if (idTasks.containsKey(id)) {
            history.remove(idTasks.get(id).data);
            removeNode(idTasks.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(Task task) {
        if (idTasks.containsKey(task.getId())) {
            remove(task.getId());
        }
        history.addLast(task);
        linkLast(task);
    }

    private void linkLast(Task task) {
        Node<Task> node;
        if (tail == null) {
            node = new Node<>(task, null, null);
            tail = node;
            head = node;
        } else {
            node = new Node<>(task, null, tail);
            tail.next = node;
            tail = node;
        }
        idTasks.put(task.getId(), node);
    }

    private void removeNode(Node<Task> node) {
        Node<Task> head = node.prev;
        Node<Task> tail = node.next;
        if (head != null) {
            idTasks.get(head.data.getId()).next = tail;
        }
        if (tail != null) {
            idTasks.get(tail.data.getId()).prev = head;
        }
        idTasks.remove(node.data.getId());
    }
}