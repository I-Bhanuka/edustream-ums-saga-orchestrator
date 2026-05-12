package com.example.edustream_saga_orchestrator.dto.responseDTO;

import com.example.edustream_saga_orchestrator.enums.BackendStudentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SagaLimitedStudentResponseDTO {

    private String studentId;

    private String firstName;

    private  String lastName;

    private String email;

    private LocalDate dob;

    private LocalDate enrollmentDate;

    private BackendStudentStatus studentStatus;

    private String courseId;
}
