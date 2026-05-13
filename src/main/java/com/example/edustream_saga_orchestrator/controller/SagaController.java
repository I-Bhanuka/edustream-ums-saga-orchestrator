package com.example.edustream_saga_orchestrator.controller;

import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaRegisterStudentToCourseIDRequestDTO;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.saga.core.SagaContext;
import com.example.edustream_saga_orchestrator.saga.core.SagaStepsExecutor;
import com.example.edustream_saga_orchestrator.saga.workflow.EnrollStudentToCourseWorkflow;
import com.example.edustream_saga_orchestrator.service.SagaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SagaController {

    private final EnrollStudentToCourseWorkflow enrollStudentToCourseWorkflow;;
    private final SagaStepsExecutor sagaStepsExecutor;

    @PostMapping("/enrollStudentToCourse")
    public ResponseEntity<ApiResponse<String>> enrollStudentToCourseEndpoint(
            @Valid @RequestBody SagaRegisterStudentToCourseIDRequestDTO requestDTO) {

        // Build the context
        SagaContext context = new SagaContext(
                UUID.randomUUID(),
                enrollStudentToCourseWorkflow.getName());

        context.put("studentId", requestDTO.getStudentId());
        context.put("courseId", requestDTO.getCourseId());

        // Execute the Saga workflow
        sagaStepsExecutor.runStepsSynchronously(
                enrollStudentToCourseWorkflow.getName(),
                enrollStudentToCourseWorkflow.getSteps(),
                context
        );


        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .success(true)
                        .message("(String) context.get(\"studentServiceClientResponse\"))")
                        .data(null)
                        .build());

    }
}
