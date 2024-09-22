package com.yandex.taskTracker.exceptions;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
