package com.example.edustream_saga_orchestrator.service;

import com.example.edustream_saga_orchestrator.client.CourseServiceClient;
import com.example.edustream_saga_orchestrator.client.StudentServiceClient;
import com.example.edustream_saga_orchestrator.dto.requestDTO.*;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
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


    // Call Student + Course Microservices to register a student into a course
    public ApiResponse<String> enrollStudentToCourseServiceMethod(SagaRegisterStudentToCourseIDRequestDTO requestDTO) {

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

            String response = studentServiceClient.registerStudentToCourseClient(bffRegisterStudentToCourseUUIDRequestDTO)
                    .getData();

            log.info("Received response from backend for enrollment: {}", response);

            return ApiResponse.<String>builder()
                    .success(true)
                    .message("Student enrolled to course successfully")
                    .data(response)
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
            }

            throw e;
        }




    }
}
