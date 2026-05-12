package com.example.edustream_saga_orchestrator.service;

import com.example.edustream_saga_orchestrator.client.CourseServiceClient;
import com.example.edustream_saga_orchestrator.client.StudentServiceClient;
import com.example.edustream_saga_orchestrator.dto.backendResponseDTO.BackendStudentResponseDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.*;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.dto.responseDTO.SagaLimitedStudentResponseDTO;
import com.example.edustream_saga_orchestrator.exception.CourseMicroServiceException;
import com.example.edustream_saga_orchestrator.exception.StudentMicroServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SagaService {

    private final StudentServiceClient studentServiceClient;

    private final CourseServiceClient courseServiceClient;

    // Call Student Microservice to get a student by ID and return the response to the frontend
    public SagaLimitedStudentResponseDTO getStudentById(SagaStudentRequestDTO bffStudentRequestDTO) {

        log.info("Get student by ID method called in BFFStudentService, will call Student MicroService to get a student by ID: {}",
                bffStudentRequestDTO.getStudentId());

        BackendStudentResponseDTO student = studentServiceClient.getStudentById(bffStudentRequestDTO)
                .getData();

        log.info("Received response from Student Service: {}", student.toString());

        // Extract the courseUUID from the student response
        SagaCourseRequestByUUIDDTO courseUUID = SagaCourseRequestByUUIDDTO.builder()
                .courseUUID(student.getCourseId())
                .build();

        String courseId = "No course enrolled";

        if (courseUUID.getCourseUUID() != null) {
            log.info("Extracted courseUUID from student response: {}", courseUUID);

            courseId = courseServiceClient.getCourseByUUID(courseUUID)
                    .getData()
                    .getCourseId();

            log.info("Retrieved course ID from course service: {}", courseId);

        } else {
            log.info("No courseUUID found in student response, skipping course ID retrieval");
        }

        // Extracting only the needed details to return to the frontend, we don't want to return the entire backend response which may contain more details than needed
        return SagaLimitedStudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .dob(student.getDob())
                .enrollmentDate(student.getEnrollmentDate())
                .studentStatus(student.getStudentStatus())
                .courseId(courseId)
                .build();

    }

    // Call Student + Course Microservices to register a student into a course
    public ApiResponse<SagaLimitedStudentResponseDTO> enrollStudentToCourseServiceMethod(SagaRegisterStudentToCourseIDRequestDTO requestDTO) {

        log.info("Enroll student to course method called in BFFStudentService, will call Course MicroService and Student MicroService");

        // Step 1: Call the course service to get the course details by ID and retrieve the course UUID
        UUID courseUUID;
        try {
            log.info("Calling course service to get course UUID by course ID: {}", requestDTO.getCourseId());

            SagaCourseRequestByIdDTO bffCourseRequestByIdDTO = SagaCourseRequestByIdDTO.builder()
                    .courseId(requestDTO.getCourseId())
                    .build();

            courseUUID = courseServiceClient.registerStudentToCourse(bffCourseRequestByIdDTO)
                    .getData();

            log.info("Received response from backend with the Course UUID {} for student registration.", courseUUID);

        } catch (CourseMicroServiceException e) {
            log.error("Error occurred while calling Course MicroService: {} - {}", e.getStatusCode(), e.getDownstreamMessage());
            throw e;
        }

        // Step 2: Call the student service to register the student to the course using the course UUID and student ID
        try {
            log.info("Calling student service to register student with ID: {} to course with UUID: {}", requestDTO.getStudentId(), courseUUID);

            SagaRegisterStudentToCourseUUIDRequestDTO bffRegisterStudentToCourseUUIDRequestDTO = SagaRegisterStudentToCourseUUIDRequestDTO.builder()
                    .courseUUID(courseUUID)
                    .studentId(requestDTO.getStudentId())
                    .build();

            ApiResponse<String> response = studentServiceClient.registerStudentToCourseClient(bffRegisterStudentToCourseUUIDRequestDTO);

            log.info("Received response from backend for enrollment: {}", response.getData());

            SagaLimitedStudentResponseDTO student = getStudentById(SagaStudentRequestDTO.builder()
                    .studentId(requestDTO.getStudentId())
                    .build());

            return ApiResponse.<SagaLimitedStudentResponseDTO>builder()
                    .success(true)
                    .message("Student with ID: " + student.getStudentId() + " enrolled to course with ID: " + requestDTO.getCourseId() + " successfully")
                    .data(student)
                    .build();

        } catch (StudentMicroServiceException e) {
            log.error("Error occurred while calling Student MicroService: {} - {}", e.getStatusCode(), e.getDownstreamMessage());

            // Call the compensation method to rollback the course registration by decrementing the number of students in the course
            try {
                log.info("Calling compensation method in Course MicroService to rollback the course registration for course UUID: {}", courseUUID);
                SagaCourseRequestByUUIDDTO bffCourseRequestByUUIDDTO = SagaCourseRequestByUUIDDTO.builder()
                        .courseUUID(courseUUID)
                        .build();

                courseServiceClient.registerStudentToCourseCompensation(bffCourseRequestByUUIDDTO);
            } catch (CourseMicroServiceException e1) {
                log.error("Error occurred while calling Course MicroService compensation method: {} - {}. Manual intervention may be required to rollback the course registration for course UUID: {}",
                        e1.getStatusCode(), e1.getDownstreamMessage(), courseUUID);

                throw e1;
            }

            throw e;
        }




    }
}
