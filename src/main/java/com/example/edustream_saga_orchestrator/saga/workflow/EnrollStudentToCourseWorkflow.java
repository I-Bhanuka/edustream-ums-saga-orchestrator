package com.example.edustream_saga_orchestrator.saga.workflow;

import com.example.edustream_saga_orchestrator.saga.core.SagaStep;
import com.example.edustream_saga_orchestrator.saga.step.AssignCourseToStudentStep;
import com.example.edustream_saga_orchestrator.saga.step.RegisterToCourseStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EnrollStudentToCourseWorkflow {

    private final RegisterToCourseStep registerToCourseStep;
    private final AssignCourseToStudentStep assignCourseToStudentStep;

    public String getName() {
        return "ENROLL_STUDENT_TO_COURSE_WORKFLOW";
    }

    public List<SagaStep> getSteps() {
        return List.of(
                registerToCourseStep,
                assignCourseToStudentStep
        );
    }

}
