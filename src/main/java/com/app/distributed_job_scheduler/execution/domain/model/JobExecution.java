package com.app.distributed_job_scheduler.execution.domain.model;

import com.app.distributed_job_scheduler.job.domain.model.Job;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_executions")
@Getter
@Setter
public class JobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "worker_id")
    private String workerId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

// this represents the number of times retry attempted after failure not the number of times it got executed till now.
    @Column(name = "attempt_number", nullable = false)
    private int attemptNumber;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "response_code")
    private Integer responseCode;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;

    @Column(name = "next_retry_time")
    private LocalDateTime nextRetryTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}