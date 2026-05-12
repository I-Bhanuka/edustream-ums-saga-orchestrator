package com.example.edustream_saga_orchestrator.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SagaStudentRequestDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;
}
