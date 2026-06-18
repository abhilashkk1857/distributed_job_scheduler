package com.app.distributed_job_scheduler.execution.domain.dtos;

import com.app.distributed_job_scheduler.execution.domain.model.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExecutionSummaryResponse(

        UUID id,

        ExecutionStatus status,

        Integer attemptNumber,

        LocalDateTime startedAt,

        LocalDateTime finishedAt

) {
}
