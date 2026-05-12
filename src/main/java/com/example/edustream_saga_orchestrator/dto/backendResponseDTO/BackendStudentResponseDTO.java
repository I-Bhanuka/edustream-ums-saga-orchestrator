package com.example.edustream_saga_orchestrator.dto.backendResponseDTO;

import com.example.edustream_saga_orchestrator.enums.BackendStudentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BackendStudentResponseDTO {

    private UUID id;

    private String studentId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String email;

    private LocalDate enrollmentDate;

    private BackendStudentStatus studentStatus;

    private UUID courseId;

    private LocalDateTime createdAt;

}
