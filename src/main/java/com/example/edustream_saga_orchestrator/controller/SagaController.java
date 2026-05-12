package com.example.edustream_saga_orchestrator.controller;

import com.example.edustream_saga_orchestrator.dto.requestDTO.SagaRegisterStudentToCourseIDRequestDTO;
import com.example.edustream_saga_orchestrator.dto.responseDTO.ApiResponse;
import com.example.edustream_saga_orchestrator.dto.responseDTO.SagaLimitedStudentResponseDTO;
import com.example.edustream_saga_orchestrator.service.SagaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SagaController {

    private final SagaService sagaService;

    @PostMapping("/enrollStudentToCourse")
    public ResponseEntity<ApiResponse<SagaLimitedStudentResponseDTO>> enrollStudentToCourseEndpoint(
            @Valid @RequestBody SagaRegisterStudentToCourseIDRequestDTO requestDTO) {

        ApiResponse<SagaLimitedStudentResponseDTO> response = sagaService.enrollStudentToCourseServiceMethod(requestDTO);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
