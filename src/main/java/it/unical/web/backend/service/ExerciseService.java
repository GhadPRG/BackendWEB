package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.ExerciseDAOImpl;
import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.service.Request.ExerciseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {

    public ResponseEntity<?> create(ExerciseRequest exerciseRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                // recupero l'user
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDAOImpl userDAO = new UserDAOImpl();
                User user = userDAO.getUserByUsername(userDetails.getUsername());

                // parsing exercise
                Exercise exercise = new Exercise(0, // it can't accept null
                        exerciseRequest.getName(), exerciseRequest.getNotes(),
                        exerciseRequest.getMuscleGroup(), exerciseRequest.getReps(),
                        exerciseRequest.getSets(), exerciseRequest.getKcalPerRep(),
                        exerciseRequest.getWeight(), user);

                // try to create exercise
                ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl();
                exerciseDAO.createExercise(exercise);
                return ResponseEntity.ok().body(exercise.getId());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"Message\": \"Error creating exercise.\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }
}
