package it.unical.web.backend.controller;

import it.unical.web.backend.service.ExerciseService;
import it.unical.web.backend.service.Request.ExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getExerciseById(@PathVariable("id") int id) {
        return exerciseService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteExerciseById(@PathVariable("id") int id) {
        return exerciseService.deleteById(id);
    }

}
