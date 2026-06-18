package com.app.distributed_job_scheduler.job.schedular.impl;

import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;
import com.app.distributed_job_scheduler.job.schedular.ScheduleCalculator;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleCalculatorImpl implements ScheduleCalculator {

    @Override
    public LocalDateTime calculateNextRun(ScheduleType scheduleType, String cronExpression, LocalDateTime scheduledTime) {

        if (scheduleType.equals(ScheduleType.ONE_TIME)) {
            return scheduledTime;
        }

        CronExpression cron;

        try {
            cron = CronExpression.parse(cronExpression);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cron expression: " + cronExpression);
        }

        return cron.next(LocalDateTime.now());

    }

}
