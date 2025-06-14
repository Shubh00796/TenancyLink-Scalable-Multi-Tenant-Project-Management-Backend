package com.Multi.tenant_SaaS_Project_Management_System.Exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}