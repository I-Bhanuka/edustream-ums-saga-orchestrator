package com.example.edustream_saga_orchestrator.saga.core;

/**
 * Contract for a single step in a saga
 */

public interface SagaStep {

    String getName();

    void executeStep(SagaContext context);
    // Will have the steps to execute including the Client calls
    // Our client calls is inside of the step

    void executeCompensate(SagaContext context);
}
