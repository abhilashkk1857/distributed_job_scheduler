package com.app.distributed_job_scheduler.job.service;

import com.app.distributed_job_scheduler.job.domain.dtos.CreateJobRequest;
import com.app.distributed_job_scheduler.job.domain.dtos.JobResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.JobSummaryResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.UpdateJobRequest;

import java.util.List;
import java.util.UUID;

public interface JobService {

    JobResponse createJob(CreateJobRequest request);

    JobResponse updateJob(UUID jobId, UpdateJobRequest request);

    void deleteJob(UUID jobId);

    JobResponse pauseJob(UUID jobId);

    JobResponse resumeJob(UUID jobId);

    JobResponse getJobById(UUID jobId);

    List<JobSummaryResponse> getAllJobs();
}
