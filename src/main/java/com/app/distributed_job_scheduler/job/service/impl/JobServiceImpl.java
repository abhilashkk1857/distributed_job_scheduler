package com.app.distributed_job_scheduler.job.service.impl;

import com.app.distributed_job_scheduler.job.domain.dtos.CreateJobRequest;
import com.app.distributed_job_scheduler.job.domain.dtos.JobResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.JobSummaryResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.UpdateJobRequest;
import com.app.distributed_job_scheduler.job.domain.model.Job;
import com.app.distributed_job_scheduler.job.domain.model.JobStatus;
import com.app.distributed_job_scheduler.job.domain.model.ScheduleType;
import com.app.distributed_job_scheduler.job.exception.JobNotFoundException;
import com.app.distributed_job_scheduler.job.domain.mapper.JobMapper;
import com.app.distributed_job_scheduler.job.repository.JobRepository;
import com.app.distributed_job_scheduler.job.schedular.impl.ScheduleCalculatorImpl;
import com.app.distributed_job_scheduler.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final ScheduleCalculatorImpl scheduleCalculator;

    @Override
    @Transactional
    public JobResponse createJob(CreateJobRequest request) {
        Job job = jobMapper.toJob(request);

        if (request.scheduleType() == ScheduleType.CRON && request.cronExpression() == null) {
            throw new IllegalArgumentException("Cron expression is required for CRON jobs");
        }

        if (request.scheduleType() == ScheduleType.ONE_TIME && request.scheduledTime() == null) {
            throw new IllegalArgumentException("Scheduled time is required for ONE_TIME jobs");
        }

        if (request.scheduleType() == ScheduleType.CRON && request.scheduledTime() != null) {
            throw new IllegalArgumentException("Scheduled time must be null for CRON jobs");
        }

        if (request.scheduleType() == ScheduleType.ONE_TIME && request.cronExpression() != null) {
            throw new IllegalArgumentException("Cron expression must be null for ONE_TIME jobs");
        }

        if (request.maxRetries() != null && request.maxRetries() < 0) {
            throw new IllegalArgumentException("Max retries cannot be negative");
        }

        job.setNextRunTime(
                scheduleCalculator.calculateNextRun(
                        job.getScheduleType(),
                        job.getCronExpression(),
                        job.getScheduledTime()
                )
        );

        Job savedJob = jobRepository.save(job);
        return jobMapper.toJobResponse(savedJob);
    }

    @Override
    @Transactional
    public JobResponse updateJob(UUID jobId, UpdateJobRequest request) {
        Job job = getJobOrThrow(jobId);
        Job updatedJob = jobMapper.toJob(request);

        job.setName(updatedJob.getName());
        job.setDescription(updatedJob.getDescription());
        job.setScheduleType(updatedJob.getScheduleType());
        job.setCronExpression(updatedJob.getCronExpression());
        job.setScheduledTime(updatedJob.getScheduledTime());
        job.setEndpointUrl(updatedJob.getEndpointUrl());
        job.setHttpMethod(updatedJob.getHttpMethod());
        job.setRequestBody(updatedJob.getRequestBody());
        job.setRequestHeaders(updatedJob.getRequestHeaders());
        job.setMaxRetries(request.maxRetries() != null ? request.maxRetries() : job.getMaxRetries());

        job.setNextRunTime(
                scheduleCalculator.calculateNextRun(
                        job.getScheduleType(),
                        job.getCronExpression(),
                        job.getScheduledTime()
                )
        );


        return jobMapper.toJobResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public void deleteJob(UUID jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        jobRepository.deleteById(jobId);
    }

    @Override
    @Transactional
    public JobResponse pauseJob(UUID jobId) {
        Job job = getJobOrThrow(jobId);
        job.setStatus(JobStatus.PAUSED);
        return jobMapper.toJobResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobResponse resumeJob(UUID jobId) {
        Job job = getJobOrThrow(jobId);
        job.setStatus(JobStatus.ACTIVE);
        return jobMapper.toJobResponse(jobRepository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(UUID jobId) {
        return jobMapper.toJobResponse(getJobOrThrow(jobId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobSummaryResponse> getAllJobs() {
        return jobRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(jobMapper::toJobSummaryResponse)
                .toList();
    }

    private Job getJobOrThrow(UUID jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
    }
}
