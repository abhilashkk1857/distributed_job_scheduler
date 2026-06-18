package com.app.distributed_job_scheduler.exception;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public record ErrorResponse(

        String message,

        HttpStatus status,

        LocalDateTime timestamp

) {
}
