package com.example.edustream_saga_orchestrator.client;

import com.example.edustream_saga_orchestrator.dto.backendResponseDTO.BackendStudentResponseDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaRegisterStudentToCourseUUIDRequestDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaStudentRequestDTO;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.exception.StudentMicroServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;


@Component
@Slf4j
public class StudentServiceClient {

    // ResClient bean
    private final RestClient restClient;

    // Endpoints
    @Value("${services.student.api.getById}")
    private String studentReadByIdEndpoint;

    @Value(("${services.student.api.registerToCourse}"))
    private String studentRegistrationToCourseEndpoint;


    public StudentServiceClient(@Qualifier("studentRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ApiResponse<BackendStudentResponseDTO> getStudentById(SagaStudentRequestDTO studentRequestDTO) {

        try {

            log.info("Calling the Student Service's {} URI to get the student of ID: {}", studentReadByIdEndpoint, studentRequestDTO.getStudentId());

            return restClient.post()
                    .uri(studentReadByIdEndpoint)
                    .body(studentRequestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            // The MS thinks that the BFF made a mistake - 4xx errors
        } catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Student MS for to get student by id: {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new StudentMicroServiceException("Student Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS has an issue processing the request - 5xx errors
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Student MS when retrieval a student: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new StudentMicroServiceException("Student Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Student MS to retrieve a student: {}", e.getMessage());
            throw new StudentMicroServiceException("Cannot reach Student Service", 503);
        }
    }

    public ApiResponse<String> registerStudentToCourseClient(SagaRegisterStudentToCourseUUIDRequestDTO request) {

        try {

            log.info("Calling the Student Service's {} URI to register the student wit ID: {}",
                    studentRegistrationToCourseEndpoint,
                    request.getStudentId());

            return restClient.post()
                    .uri(studentRegistrationToCourseEndpoint)
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            // The MS thinks that the BFF made a mistake - 4xx errors
        } catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Student MS for to assign a course for the student by id: {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new StudentMicroServiceException("Student Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS has an issue processing the request - 5xx errors
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Student MS when registering a student: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new StudentMicroServiceException("Student Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Student MS to register a student to a course: {}", e.getMessage());
            throw new StudentMicroServiceException("Cannot reach Student Service", 503);
        }
    }

}
