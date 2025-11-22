package com.workhub.server.exception.custom;

import java.util.UUID;

public class TaskCommentNotFoundException extends RuntimeException {
    public TaskCommentNotFoundException(UUID id) {
        super("Task comment with id " + id + " not found");
    }
}

