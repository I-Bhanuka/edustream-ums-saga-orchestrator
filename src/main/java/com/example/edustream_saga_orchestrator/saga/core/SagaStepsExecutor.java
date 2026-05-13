package com.example.edustream_saga_orchestrator.saga.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SagaStepsExecutor {

    public void runStepsSynchronously(String workflowName,
                                      List<SagaStep> steps,
                                      SagaContext context) {

        log.info("[{}] Starting Saga: {}", context.getSagaId(), workflowName);

        // Will be helpful when we need to backtrack
        // If an error happened when executedCount was 2
        // Then it will reduce 1 from it, which will exactly match with the index in the steps list
        // Then that step's compensate method will be called
        // Current index represents the current step/index we are in the list of step
        int executedCount = 0;

        for (SagaStep step : steps) {
            try {
                log.info("[{}] Executing step: {}", context.getSagaId(), step.getName());
                step.executeStep(context); // Execute the main logic
                context.markStepCompleted(step.getName()); // Mark this step as completed in the context
                executedCount++; // Increment the count of successfully executed steps. Meaning moving onto the next step
                log.info("[{}] Completed step: {}", context.getSagaId(), step.getName());

            } catch (Exception executionException) {
                log.error("[{}] Step failed: {}. Compensating {} step(s)",
                        context.getSagaId(), step.getName(), executedCount);

                for (int i = executedCount - 1; i >= 0; i--) {
                    SagaStep stepToCompensate = steps.get(i);

                    try {
                        log.info("[{}] Compensating step: {}", context.getSagaId(), stepToCompensate.getName());
                        stepToCompensate.executeCompensate(context); // Execute the compensation logic
                        log.info("[{}] Completed succeeded: {}", context.getSagaId(), stepToCompensate.getName());

                    } catch (Exception compensationException) {
                        log.error("[{}] CRITICAL: Compensation failed for step: {}. Manual intervention required.",
                                context.getSagaId(), stepToCompensate.getName());
                    }
                }

                throw executionException;
            }
        }

        log.info("[{}] Saga completed successfully: {}", context.getSagaId(), workflowName);

    }
}
