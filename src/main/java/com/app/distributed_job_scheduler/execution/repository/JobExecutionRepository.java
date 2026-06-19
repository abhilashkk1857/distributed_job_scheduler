package com.app.distributed_job_scheduler.execution.repository;

import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {

    @Query("select je from JobExecution je where je.job.id = :jobId order by je.createdAt desc")
    List<JobExecution> findByJobId(@Param("jobId") UUID jobId);
}
