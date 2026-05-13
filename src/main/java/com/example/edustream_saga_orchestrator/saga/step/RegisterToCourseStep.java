package com.example.edustream_saga_orchestrator.saga.step;

import com.example.edustream_saga_orchestrator.client.CourseServiceClient;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaCourseRequestByIdDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaCourseRequestByUUIDDTO;
import com.example.edustream_saga_orchestrator.saga.core.SagaContext;
import com.example.edustream_saga_orchestrator.saga.core.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterToCourseStep implements SagaStep {

    private final CourseServiceClient courseServiceClient;

    @Override
    public String getName() {
        return "REGISTER_TO_COURSE";
    }

    @Override
    public void executeStep(SagaContext context) {

        // Extract Course ID from the SagaContext for the Course MS call
        String courseId = (String) context.get("courseId");

        // Call the Course MS to register the student to the course
        log.info("[{}] Register to course: {}", context.getSagaId(), courseId);

        UUID courseUUID = courseServiceClient
                .registerStudentToCourse(SagaCourseRequestByIdDTO.builder()
                        .courseId(courseId)
                        .build())
                .getData();


        log.info("[{}] Saving course UUID into the SagaContext: {}", context.getSagaId(), courseUUID);
        context.put("courseUUID", courseUUID);
    }

    @Override
    public void executeCompensate(SagaContext context) {

        // Extract Course UUID from the SagaContext
        UUID courseUUID = (UUID) context.get("courseUUID");

        log.info("[{}] Compensating — decrementing count for courseUUID: {}", context.getSagaId(), courseUUID);

        courseServiceClient.registerStudentToCourseCompensation(SagaCourseRequestByUUIDDTO.builder()
                .courseUUID(courseUUID)
                .build());
    }




}
