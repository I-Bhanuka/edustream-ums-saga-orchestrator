package com.example.edustream_saga_orchestrator.saga.core;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the context of a saga execution, holding shared data and state
 * across all steps of the saga.
 */

@Getter
public class SagaContext {

    private final UUID sagaId;
    private final String sagaName;
    private final Map<String, Object> data;
    private final List<String> completedSteps;

    // The instantiation will happen with the Saga ID and the Saga Name
    public SagaContext(UUID sagaId, String sagaName) {
        this.sagaId = UUID.randomUUID();
        this.sagaName = sagaName;
        this.data = new java.util.HashMap<>();
        this.completedSteps = new java.util.ArrayList<>();
    }

    // Method to add data to the context
    public void put(String key, Object value) {
        data.put(key, value);
    }

    // Method to retrieve data from the context
    public Object get(String key) {
        return data.get(key);
    }

    // Called by the executor after each successful step
    public void markStepCompleted(String stepName) {
        completedSteps.add(stepName);
    }






}
