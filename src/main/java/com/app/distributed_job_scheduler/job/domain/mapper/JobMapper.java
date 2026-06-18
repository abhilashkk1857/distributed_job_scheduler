package com.app.distributed_job_scheduler.job.domain.mapper;

import com.app.distributed_job_scheduler.job.domain.dtos.CreateJobRequest;
import com.app.distributed_job_scheduler.job.domain.dtos.JobResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.JobSummaryResponse;
import com.app.distributed_job_scheduler.job.domain.dtos.UpdateJobRequest;
import com.app.distributed_job_scheduler.job.domain.model.Job;
import com.app.distributed_job_scheduler.job.domain.model.JobStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobMapper {

    private final ObjectMapper objectMapper;

    public Job toJob(CreateJobRequest request) {
        return buildJob(
                request.name(),
                request.description(),
                request.scheduleType(),
                request.cronExpression(),
                request.scheduledTime(),
                request.endpointUrl(),
                request.httpMethod(),
                request.requestBody(),
                request.requestHeaders(),
                request.maxRetries()
        );
    }

    public Job toJob(UpdateJobRequest request) {
        return buildJob(
                request.name(),
                request.description(),
                request.scheduleType(),
                request.cronExpression(),
                request.scheduledTime(),
                request.endpointUrl(),
                request.httpMethod(),
                request.requestBody(),
                request.requestHeaders(),
                request.maxRetries()
        );
    }

    public JobResponse toJobResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getName(),
                job.getDescription(),
                job.getScheduleType(),
                job.getCronExpression(),
                job.getScheduledTime(),
                job.getEndpointUrl(),
                job.getHttpMethod(),
                job.getStatus(),
                job.getNextRunTime(),
                job.getMaxRetries(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    public JobSummaryResponse toJobSummaryResponse(Job job) {
        return new JobSummaryResponse(
                job.getId(),
                job.getName(),
                job.getScheduleType(),
                job.getStatus(),
                job.getNextRunTime()
        );
    }

    private Job buildJob(
            String name,
            String description,
            com.app.distributed_job_scheduler.job.domain.model.ScheduleType scheduleType,
            String cronExpression,
            java.time.LocalDateTime scheduledTime,
            String endpointUrl,
            com.app.distributed_job_scheduler.execution.domain.model.HttpMethod httpMethod,
            java.util.Map<String, Object> requestBody,
            java.util.Map<String, String> requestHeaders,
            Integer maxRetries
    ) {
        Job job = new Job();
        job.setName(name);
        job.setDescription(description);
        job.setScheduleType(scheduleType);
        job.setCronExpression(cronExpression);
        job.setScheduledTime(scheduledTime);
        job.setEndpointUrl(endpointUrl);
        job.setHttpMethod(httpMethod);
        job.setRequestBody(toJson(requestBody));
        job.setRequestHeaders(toJson(requestHeaders));
        job.setStatus(JobStatus.ACTIVE);
        job.setMaxRetries(maxRetries != null ? maxRetries : 5);
        // TODO: Delegate nextRunTime calculation to a future ScheduleCalculator component.
        job.setNextRunTime(null);
        return job;
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize job payload", ex);
        }
    }
}
