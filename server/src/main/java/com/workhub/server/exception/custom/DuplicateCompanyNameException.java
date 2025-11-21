package com.workhub.server.exception.custom;

public class DuplicateCompanyNameException extends RuntimeException {
    public DuplicateCompanyNameException(String name) {
        super("Company with name '" + name + "' already exists");
    }
}

