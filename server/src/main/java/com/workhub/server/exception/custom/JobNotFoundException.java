package com.workhub.server.exception.custom;

import java.util.UUID;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(UUID id) {
        super("Job with id " + id + " not found");
    }
}


