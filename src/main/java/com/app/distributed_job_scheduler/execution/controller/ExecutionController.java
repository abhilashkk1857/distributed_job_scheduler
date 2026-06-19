package com.app.distributed_job_scheduler.execution.controller;

import com.app.distributed_job_scheduler.execution.domain.model.JobExecution;
import com.app.distributed_job_scheduler.execution.domain.dtos.ExecutionSummaryResponse;
import com.app.distributed_job_scheduler.execution.domain.dtos.JobExecutionResponse;
import com.app.distributed_job_scheduler.execution.domain.mapper.ExecutionMapper;
import com.app.distributed_job_scheduler.execution.service.ExecutionService;
import com.app.distributed_job_scheduler.job.domain.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;
    private final ExecutionMapper executionMapper;



    @PostMapping("/executions/{id}/running")
    public ResponseEntity<JobExecutionResponse> markRunning(@PathVariable UUID id) {
        return ResponseEntity.ok(executionMapper.toResponse(executionService.markRunning(id)));
    }

    @PostMapping("/executions/{id}/success")
    public ResponseEntity<JobExecutionResponse> markSuccess(
            @PathVariable UUID id,
            @RequestParam Integer responseCode,
            @RequestParam(required = false) String responseMessage,
            @RequestParam Long executionDurationMs
    ) {
        return ResponseEntity.ok(executionMapper.toResponse(
                executionService.markSuccess(id, responseCode, responseMessage, executionDurationMs)
        ));
    }

    @PostMapping("/executions/{id}/failure")
    public ResponseEntity<JobExecutionResponse> markFailure(
            @PathVariable UUID id,
            @RequestParam String errorMessage
    ) {
        return ResponseEntity.ok(executionMapper.toResponse(executionService.markFailure(id, errorMessage)));
    }

    @GetMapping("/executions/{id}")
    public ResponseEntity<JobExecutionResponse> getExecution(@PathVariable UUID id) {
        return ResponseEntity.ok(executionService.getExecution(id));
    }

    @GetMapping("/jobs/{jobId}/executions")
    public ResponseEntity<List<ExecutionSummaryResponse>> getExecutionsByJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(executionService.getExecutionsByJob(jobId));
    }
}
