package com.app.distributed_job_scheduler.execution.service;

import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import com.app.distributed_job_scheduler.execution.domain.dtos.ExecutionSummaryResponse;
import com.app.distributed_job_scheduler.execution.domain.dtos.JobExecutionResponse;
import com.app.distributed_job_scheduler.job.domain.model.Job;

import java.util.List;
import java.util.UUID;

public interface ExecutionService {

    JobExecution createExecution(UUID jobId);

    JobExecution markRunning(UUID executionId);

    JobExecution markSuccess(UUID executionId, Integer responseCode, String responseMessage, Long executionDurationMs);

    JobExecution markFailure(UUID executionId, String errorMessage);

    JobExecutionResponse getExecution(UUID executionId);

    List<ExecutionSummaryResponse> getExecutionsByJob(UUID jobId);
}
