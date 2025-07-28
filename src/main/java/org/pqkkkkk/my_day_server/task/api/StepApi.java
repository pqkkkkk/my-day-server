package org.pqkkkkk.my_day_server.task.api;

import org.pqkkkkk.my_day_server.common.ApiResponse;
import org.pqkkkkk.my_day_server.task.api.Request.CreateStepRequest;
import org.pqkkkkk.my_day_server.task.api.Request.UpdateStepRequest;
import org.pqkkkkk.my_day_server.task.dto.DTO.StepDTO;
import org.pqkkkkk.my_day_server.task.entity.Step;
import org.pqkkkkk.my_day_server.task.service.StepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/step")
public class StepApi {
    private final StepService stepService;

    public StepApi(StepService stepService) {
        this.stepService = stepService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StepDTO>> createStep(@Valid @RequestBody CreateStepRequest request){
        Step step = CreateStepRequest.toEntity(request);

        Step createdStep = stepService.createStep(step);
        StepDTO stepDTO = StepDTO.fromEntity(createdStep);

        ApiResponse<StepDTO> response = new ApiResponse<StepDTO>(stepDTO, true, HttpStatus.CREATED.value(), "Step created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StepDTO>> updateStep(@PathVariable("id") Long stepId,
                                                        @Valid @RequestBody UpdateStepRequest request){
        Step step = UpdateStepRequest.toEntity(request);
        step.setStepId(stepId);

        Step updatedStep = stepService.updateStep(step);
        StepDTO stepDTO = StepDTO.fromEntity(updatedStep);

        ApiResponse<StepDTO> response = new ApiResponse<StepDTO>(stepDTO, true, HttpStatus.OK.value(), "Step updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStep(@PathVariable("id") Long stepId){
        Integer deletedCount = stepService.deleteStep(stepId);

        ApiResponse<Void> response = new ApiResponse<>(null, deletedCount > 0, HttpStatus.OK.value(),"Step deleted successfully");

        return ResponseEntity.ok(response);
    }
}