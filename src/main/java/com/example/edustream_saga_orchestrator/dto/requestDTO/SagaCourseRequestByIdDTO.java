package com.example.edustream_saga_orchestrator.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SagaCourseRequestByIdDTO {

    @NotBlank(message = "courseId is required")
    private  String courseId;
}
