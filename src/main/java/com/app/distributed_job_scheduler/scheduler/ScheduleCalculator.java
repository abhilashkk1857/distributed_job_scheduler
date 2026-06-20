package com.app.distributed_job_scheduler.scheduler;

import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;

import java.time.LocalDateTime;

public interface ScheduleCalculator {
    LocalDateTime calculateNextRun(
            ScheduleType scheduleType,
            String cronExpression,
            LocalDateTime scheduledTime
    );
}
