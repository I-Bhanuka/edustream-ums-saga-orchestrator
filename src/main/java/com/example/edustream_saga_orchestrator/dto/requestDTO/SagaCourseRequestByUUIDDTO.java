package com.example.edustream_saga_orchestrator.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SagaCourseRequestByUUIDDTO {

    @NotNull(message = "course UUID is required")
    private UUID courseUUID;
}
