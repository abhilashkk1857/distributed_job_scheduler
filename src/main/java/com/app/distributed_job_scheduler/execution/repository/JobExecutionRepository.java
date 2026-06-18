package com.app.distributed_job_scheduler.execution.repository;

import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {
}
