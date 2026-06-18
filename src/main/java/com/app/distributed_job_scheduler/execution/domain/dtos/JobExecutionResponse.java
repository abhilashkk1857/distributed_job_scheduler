package com.app.distributed_job_scheduler.execution.domain.dtos;

import com.app.distributed_job_scheduler.execution.domain.model.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobExecutionResponse(

        UUID id,

        UUID jobId,

        String workerId,

        ExecutionStatus status,

        Integer attemptNumber,

        LocalDateTime startedAt,

        LocalDateTime finishedAt,

        Integer responseCode,

        String responseMessage,

        String errorMessage,

        Long executionDurationMs,

        LocalDateTime nextRetryTime,

        LocalDateTime createdAt

) {
}