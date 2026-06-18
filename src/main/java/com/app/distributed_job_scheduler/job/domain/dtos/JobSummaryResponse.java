package com.app.distributed_job_scheduler.job.domain.dtos;

import com.app.distributed_job_scheduler.job.domain.model.JobStatus;
import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobSummaryResponse(

        UUID id,

        String name,

        ScheduleType scheduleType,

        JobStatus status,

        LocalDateTime nextRunTime

) {
}