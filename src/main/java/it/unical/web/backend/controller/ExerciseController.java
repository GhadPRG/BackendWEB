package it.unical.web.backend.controller;

import it.unical.web.backend.service.ExerciseService;
import it.unical.web.backend.service.Request.ExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    ExerciseService exerciseService;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createExercise(@RequestBody ExerciseRequest exerciseRequest) {
        return exerciseService.create(exerciseRequest);
    }
}
