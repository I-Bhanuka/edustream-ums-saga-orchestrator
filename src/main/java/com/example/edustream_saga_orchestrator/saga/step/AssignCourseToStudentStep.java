package com.example.edustream_saga_orchestrator.saga.step;

import com.example.edustream_saga_orchestrator.client.StudentServiceClient;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaRegisterStudentToCourseUUIDRequestDTO;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.saga.core.SagaContext;
import com.example.edustream_saga_orchestrator.saga.core.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AssignCourseToStudentStep implements SagaStep {

    private final StudentServiceClient studentServiceClient;

    @Override
    public String getName() {
        return "ASSIGN_COURSE_TO_STUDENT";
    }

    @Override
    public void executeStep(SagaContext context) {

        // Extract Student ID and Course UUID from the SagaContext for the Student MS call
        String studentId = (String) context.get("studentId");
        UUID courseUUID = (UUID) context.get("courseUUID");

        log.info("[{}] Assigning courseUUID: {} to studentId: {}", context.getSagaId(), courseUUID, studentId);

        String response = studentServiceClient.registerStudentToCourseClient(SagaRegisterStudentToCourseUUIDRequestDTO.builder()
                        .studentId(studentId)
                        .courseUUID(courseUUID)
                        .build()).getData();

        context.put("studentServiceClientResponse", response);
    }

    @Override
    public void executeCompensate(SagaContext context) {
        // Student record was never changed if this step failed — nothing to undo
        log.info("[{}] No compensation needed for step: {}", context.getSagaId(), getName());
    }
}
