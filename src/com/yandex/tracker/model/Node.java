package com.yandex.tracker.model;

public class Node<T extends Task> {
    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data,Node<T> next, Node<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
