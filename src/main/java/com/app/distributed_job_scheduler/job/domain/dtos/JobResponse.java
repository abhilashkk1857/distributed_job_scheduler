package com.app.distributed_job_scheduler.job.domain.dtos;

import com.app.distributed_job_scheduler.execution.domain.model.HttpMethod;
import com.app.distributed_job_scheduler.job.domain.model.JobStatus;
import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobResponse(

        UUID id,

        String name,

        String description,

        ScheduleType scheduleType,

        String cronExpression,

        LocalDateTime scheduledTime,

        String endpointUrl,

        HttpMethod httpMethod,

        JobStatus status,

        LocalDateTime nextRunTime,

        Integer maxRetries,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}