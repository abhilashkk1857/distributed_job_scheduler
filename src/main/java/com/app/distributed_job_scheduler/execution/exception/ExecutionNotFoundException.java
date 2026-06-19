package com.app.distributed_job_scheduler.execution.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExecutionNotFoundException extends RuntimeException {

    private final UUID executionId;

    public ExecutionNotFoundException(UUID executionId) {
        super("Execution not found with id: " + executionId);
        this.executionId = executionId;
    }
}
