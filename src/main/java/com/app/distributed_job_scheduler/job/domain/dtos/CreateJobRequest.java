package com.app.distributed_job_scheduler.job.domain.dtos;

import com.app.distributed_job_scheduler.execution.domain.model.HttpMethod;
import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateJobRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        ScheduleType scheduleType,

        String cronExpression,

        LocalDateTime scheduledTime,

        @NotBlank
        String endpointUrl,

        @NotNull
        HttpMethod httpMethod,

        Map<String, Object> requestBody,

        Map<String, String> requestHeaders,

        Integer maxRetries

) {
}