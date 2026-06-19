package com.app.distributed_job_scheduler.execution.domain.mapper;

import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import com.app.distributed_job_scheduler.execution.domain.dtos.ExecutionSummaryResponse;
import com.app.distributed_job_scheduler.execution.domain.dtos.JobExecutionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExecutionMapper {

    public JobExecutionResponse toResponse(JobExecution execution) {
        if (execution == null) {
            return null;
        }

        return new JobExecutionResponse(
                execution.getId(),
                execution.getJob() != null ? execution.getJob().getId() : null,
                execution.getWorkerId(),
                execution.getStatus(),
                execution.getAttemptNumber(),
                execution.getStartedAt(),
                execution.getFinishedAt(),
                execution.getResponseCode(),
                execution.getResponseMessage(),
                execution.getErrorMessage(),
                execution.getExecutionDurationMs(),
                execution.getNextRetryTime(),
                execution.getCreatedAt()
        );
    }

    public ExecutionSummaryResponse toSummary(JobExecution execution) {
        if (execution == null) {
            return null;
        }

        return new ExecutionSummaryResponse(
                execution.getId(),
                execution.getStatus(),
                execution.getAttemptNumber(),
                execution.getStartedAt(),
                execution.getFinishedAt()
        );
    }

    public List<ExecutionSummaryResponse> toSummaryList(List<JobExecution> executions) {
        if (executions == null) {
            return List.of();
        }

        return executions.stream()
                .map(this::toSummary)
                .toList();
    }
}
