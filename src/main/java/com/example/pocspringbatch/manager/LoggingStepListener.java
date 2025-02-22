package com.example.pocspringbatch.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class LoggingStepListener implements StepExecutionListener {


    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step \'{}\' starting with context: {}", stepExecution.getStepName(), stepExecution.getExecutionContext());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step \'{}\' finished with status: {}", stepExecution.getStepName(), stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }
}