package com.app.distributed_job_scheduler.execution.domain.model;

public enum ExecutionStatus {

    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    RETRY_WAITING,
    DEAD

}
