package com.example.edustream_saga_orchestrator.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SagaRegisterStudentToCourseIDRequestDTO {

    @NotNull(message = "Student ID cannot be null")
    @NotBlank(message = "Student ID cannot be blank")
    private String studentId;

    @NotNull(message = "Course UUID cannot be null")
    private String courseId;
}
