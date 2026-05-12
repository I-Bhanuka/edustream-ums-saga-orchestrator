package com.example.edustream_bff.client;

import com.example.edustream_bff.dto.backendResponse.BackendCourseDTO;
import com.example.edustream_bff.dto.backendResponse.PageResponseDTO;
import com.example.edustream_bff.dto.requestDTO.BFFCourseRequestByIdDTO;
import com.example.edustream_bff.dto.requestDTO.BFFCourseRequestByUUIDDTO;
import com.example.edustream_bff.dto.requestDTO.BFFRegisterCourseRequestDTO;
import com.example.edustream_bff.dto.responseDTO.ApiResponse;
import com.example.edustream_bff.dto.responseDTO.BFFRegisterCourseResponseDTO;
import com.example.edustream_bff.exception.CourseMicroServiceException;
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
    @Value("${services.course.api.create}")
    private String courseCreateEndpoint;

    @Value("${services.course.api.getAll}")
    private String courseReadAllEndpoint;

    @Value("${services.course.api.getById}")
    private String courseReadByIdEndpoint;

    @Value("${services.course.api.getByCourseUUID}")
    private String courseReadByUUIDEndpoint;

    @Value("${services.course.api.registerToCourse}")
    private String courseRegisterStudentEndpoint;

    @Value("${services.course.api.registerToCourseCompensation}")
    private String courseRegisterStudentCompensationEndpoint;


    public CourseServiceClient(@Qualifier("courseRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ApiResponse<BFFRegisterCourseResponseDTO> registerCourse(BFFRegisterCourseRequestDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to register a course", courseCreateEndpoint);

            return restClient.post()
                    .uri(courseCreateEndpoint)
                    .body(requestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                          }
                    );

            // The MS thinks that the BFF made a mistake - 4xx error
        } catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for course registration: {} - {}",
                    e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when course registration: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to register a course: {}", e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }

    public ApiResponse<PageResponseDTO<Object>> getAllCourses() {

        try {
            log.info("Calling the Course Service's {} URI to get all courses", courseReadAllEndpoint);

            return restClient.post()
                    .uri(courseReadAllEndpoint)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            // The MS thinks that the BFF made a mistake - 4xx error
        } catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for to get all courses: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when retrieval of all courses: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to retrieve all courses: {}", e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }

    public ApiResponse<Object> getCourseById(BFFCourseRequestByIdDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to get a course by ID: {}", courseReadByIdEndpoint, requestDTO.getCourseId());

            return restClient.post()
                    .uri(courseReadByIdEndpoint)
                    .body(requestDTO)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } // The MS thinks that the BFF made a mistake - 4xx error
        catch (HttpClientErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("BFF error when connecting to Course MS for to get course by id: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service rejected the request",
                    e.getStatusCode().value(),
                    downStreamMessage);

            // The MS encountered an error while processing the request - 5xx error
        } catch (HttpServerErrorException e) {
            String downStreamMessage = e.getResponseBodyAsString();
            log.error("Server error from Course MS when retrieval a course: {} - {}", e.getStatusCode().value(),
                    downStreamMessage);
            throw new CourseMicroServiceException("Course Service encountered an error.",
                    e.getStatusCode().value(),
                    downStreamMessage);

        } catch (ResourceAccessException e) {
            log.error("Network error reaching Course MS to retrieve a course: {}", e.getMessage());
            throw new CourseMicroServiceException("Cannot reach Course Service", 503);
        }
    }

    public ApiResponse<BackendCourseDTO> getCourseByUUID(BFFCourseRequestByUUIDDTO requestDTO) {

        try {
            log.info("Calling the Course Service's {} URI to get a course by UUID: {}", courseReadByIdEndpoint, requestDTO.getCourseUUID());

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

    public ApiResponse<UUID> registerStudentToCourse(BFFCourseRequestByIdDTO requestDTO) {

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

    public ApiResponse<String> registerStudentToCourseCompensation(BFFCourseRequestByUUIDDTO requestDTO) {

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
