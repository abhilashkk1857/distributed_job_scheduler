package com.app.distributed_job_scheduler.execution.service.impl;

import com.app.distributed_job_scheduler.execution.domain.model.ExecutionStatus;
import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import com.app.distributed_job_scheduler.execution.exception.ExecutionNotFoundException;
import com.app.distributed_job_scheduler.execution.repository.JobExecutionRepository;
import com.app.distributed_job_scheduler.execution.service.ExecutionService;
import com.app.distributed_job_scheduler.execution.domain.dtos.ExecutionSummaryResponse;
import com.app.distributed_job_scheduler.execution.domain.dtos.JobExecutionResponse;
import com.app.distributed_job_scheduler.execution.domain.mapper.ExecutionMapper;
import com.app.distributed_job_scheduler.job.domain.model.Job;
import com.app.distributed_job_scheduler.job.exception.JobNotFoundException;
import com.app.distributed_job_scheduler.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

    private final JobExecutionRepository jobExecutionRepository;
    private final JobRepository jobRepository;
    private final ExecutionMapper executionMapper;

    @Override
    @Transactional
    public JobExecution createExecution(UUID jobId) {
        if (jobId == null) {
            throw new IllegalArgumentException("Job id is required to create an execution");
        }

        Job managedJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        JobExecution execution = new JobExecution();
        execution.setJob(managedJob);
        execution.setStatus(ExecutionStatus.PENDING);
        execution.setAttemptNumber(1);

        return jobExecutionRepository.save(execution);
    }

    @Override
    @Transactional
    public JobExecution markRunning(UUID executionId) {
        JobExecution execution = getExecutionEntityOrThrow(executionId);
        validateTransaction(execution, ExecutionStatus.PENDING, ExecutionStatus.RUNNING);

        execution.setStatus(ExecutionStatus.RUNNING);
        execution.setStartedAt(LocalDateTime.now());
        return jobExecutionRepository.save(execution);
    }

    @Override
    @Transactional
    public JobExecution markSuccess(UUID executionId, Integer responseCode, String responseMessage, Long executionDurationMs) {
        JobExecution execution = getExecutionEntityOrThrow(executionId);
        validateTransaction(execution, ExecutionStatus.RUNNING, ExecutionStatus.SUCCESS);

        execution.setStatus(ExecutionStatus.SUCCESS);
        execution.setFinishedAt(LocalDateTime.now());
        execution.setResponseCode(responseCode);
        execution.setResponseMessage(responseMessage);
        execution.setExecutionDurationMs(executionDurationMs);
        execution.setErrorMessage(null);
        return jobExecutionRepository.save(execution);
    }

    @Override
    @Transactional
    public JobExecution markFailure(UUID executionId, String errorMessage) {
        JobExecution execution = getExecutionEntityOrThrow(executionId);
        validateTransaction(execution, ExecutionStatus.RUNNING, ExecutionStatus.FAILED);

        execution.setStatus(ExecutionStatus.FAILED);
        execution.setFinishedAt(LocalDateTime.now());
        execution.setErrorMessage(errorMessage);
        execution.setResponseCode(null);
        execution.setResponseMessage(null);
        execution.setExecutionDurationMs(null);
        return jobExecutionRepository.save(execution);
    }

    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getExecution(UUID executionId) {
        return executionMapper.toResponse(getExecutionEntityOrThrow(executionId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionSummaryResponse> getExecutionsByJob(UUID jobId) {
        return jobExecutionRepository.findByJobId(jobId).stream()
                .map(executionMapper::toSummary)
                .toList();
    }

    private JobExecution getExecutionEntityOrThrow(UUID executionId) {
        return jobExecutionRepository.findById(executionId)
                .orElseThrow(() -> new ExecutionNotFoundException(executionId));
    }

    private void validateTransaction(JobExecution execution, ExecutionStatus expected, ExecutionStatus target) {
        if (execution.getStatus() != expected) {
            throw new IllegalArgumentException(
                    "Execution " + execution.getId() + " cannot transition from " + execution.getStatus() + " to " + target
            );
        }
    }
}
