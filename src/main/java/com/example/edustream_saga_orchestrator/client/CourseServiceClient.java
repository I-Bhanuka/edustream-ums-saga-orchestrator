package com.example.edustream_saga_orchestrator.client;

import com.example.edustream_saga_orchestrator.dto.backendResponseDTO.BackendCourseDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaCourseRequestByIdDTO;
import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaCourseRequestByUUIDDTO;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.exception.CourseMicroServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@Slf4j
public class CourseServiceClient {

    private final RestClient restClient;

    // Endpoints
    @Value("${services.course.api.getByCourseUUID}")
    private String courseReadByUUIDEndpoint;

    @Value("${services.course.api.registerToCourse}")
    private String courseRegisterStudentEndpoint;

    @Value("${services.course.api.registerToCourseCompensation}")
    private String courseRegisterStudentCompensationEndpoint;


    public CourseServiceClient(@Qualifier("courseRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ApiResponse<BackendCourseDTO> getCourseByUUID(SagaCourseRequestByUUIDDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to get a course by UUID: {}", courseReadByUUIDEndpoint, requestDTO.getCourseUUID());

            return restClient.post()
                    .uri(courseReadByUUIDEndpoint)
                    .body(requestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } // The MS thinks that the BFF made a mistake - 4xx error
        catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for to get course by UUID: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when retrieval a course by UUID: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to retrieve a course by UUID: {}", e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }


    public ApiResponse<UUID> registerStudentToCourse(SagaCourseRequestByIdDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to register a student for a course with ID: {}",
                    courseRegisterStudentEndpoint,
                    requestDTO.getCourseId());

            return restClient.post()
                    .uri(courseRegisterStudentEndpoint)
                    .body(requestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            // The MS thinks that the BFF made a mistake - 4xx error
        }  catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for to register a student into a course. {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when register a student for a course. {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to register a student for a course. {}",
                    e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }

    public ApiResponse<String> registerStudentToCourseCompensation(SagaCourseRequestByUUIDDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to compensate the previous student registration for the course with UUID:  {}",
                    courseRegisterStudentCompensationEndpoint,
                    requestDTO.getCourseUUID());

            return restClient.post()
                    .uri(courseRegisterStudentCompensationEndpoint)
                    .body(requestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            // The MS thinks that the BFF made a mistake - 4xx error
        }  catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for register a student compensation. {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when compensating the student registration. {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to compensate student registration. {}",
                    e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }


}
