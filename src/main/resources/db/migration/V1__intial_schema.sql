CREATE TABLE IF NOT EXISTS jobs (

                                    id UUID PRIMARY KEY,

                                    name varchar(255) NOT NULL,

    description TEXT,

    schedule_type VARCHAR(20) NOT NULL
    CHECK (
              schedule_type IN ('ONE_TIME', 'CRON')
    ),

    cron_expression VARCHAR(100),

    scheduled_time TIMESTAMP,

    endpoint_url TEXT NOT NULL,

    http_method VARCHAR(10) NOT NULL,

    request_body TEXT,

    request_headers TEXT,

    status VARCHAR(20) NOT NULL
    CHECK (
              status IN ('ACTIVE', 'PAUSED')
    ),

    next_run_time TIMESTAMP,

    max_retries INT NOT NULL DEFAULT 5
    CHECK (max_retries >= 0),

    created_at TIMESTAMP NOT NULL ,

    updated_at TIMESTAMP NOT NULL

    );



CREATE TABLE IF NOT EXISTS job_executions (

                                              id UUID PRIMARY KEY,

                                              job_id UUID NOT NULL,

                                              worker_id VARCHAR(100),

    status VARCHAR(20) NOT NULL
    CHECK (
              status IN (
              'PENDING',
              'RUNNING',
              'SUCCESS',
              'FAILED',
              'RETRY_WAITING',
              'DEAD'
                        )
    ),

    attempt_number INT NOT NULL,

    started_at TIMESTAMP WITH TIME ZONE,

    finished_at TIMESTAMP WITH TIME ZONE,

                              response_code INT,

                              response_message TEXT,

                              error_message TEXT,

                              execution_duration_ms BIGINT,

                              next_retry_time TIMESTAMP,

                              created_at TIMESTAMP NOT NULL,

                              CONSTRAINT fk_job_execution_job
                              FOREIGN KEY (job_id)
    REFERENCES jobs(id)
                          ON DELETE CASCADE

    );


CREATE INDEX idx_jobs_next_run_time ON jobs(next_run_time);

CREATE INDEX idx_jobs_status ON jobs(status);

CREATE INDEX idx_job_executions_job_id ON job_executions(job_id);

CREATE INDEX idx_job_executions_status ON job_executions(status);

