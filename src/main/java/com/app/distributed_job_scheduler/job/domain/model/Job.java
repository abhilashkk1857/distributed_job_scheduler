package com.app.distributed_job_scheduler.job.domain.model;


import com.app.distributed_job_scheduler.execution.domain.model.HttpMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name",  nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule_type",  nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "endpoint_url", nullable = false)
    private String endpointUrl;

    @Column(name = "http_method",  nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "request_headers")
    private String requestHeaders;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "next_run_time")
    private LocalDateTime nextRunTime;

    @Column(name = "max_retries", nullable = false)
    private Integer maxRetries;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",  nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
