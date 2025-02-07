package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.ExerciseDAOImpl;
import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.service.Request.ExerciseRequest;
import it.unical.web.backend.service.Response.ExerciseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
                        exerciseRequest.getSets(), exerciseRequest.getMet(),
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


    public ResponseEntity<?> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                // fetch user id
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDAOImpl userDAO = new UserDAOImpl();
                int userId = userDAO.getUserByUsername(userDetails.getUsername()).getId();

                // fetch all exercises
                ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl();
                List<ExerciseResponse> exercises = exerciseDAO.getAllExercisesByUser(userId).stream()
                        .map(ExerciseResponse::new)
                        .toList();

                if (!exercises.isEmpty()) {
                    return ResponseEntity.ok().body(exercises);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"Message\": \"Error fetching exercise.\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }

    public ResponseEntity<?> getById(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                if (id < 0){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Message\": \"Id must be positive.\"}");
                }

                // fetch user id
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDAOImpl userDAO = new UserDAOImpl();
                int userId = userDAO.getUserByUsername(userDetails.getUsername()).getId();

                // fetch exercise
                ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl();
                Exercise exercise = exerciseDAO.getExerciseById(id, userId);

                if (exercise == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Message\": \"Exercise not found.\"}");
                }

                return ResponseEntity.ok().body(new ExerciseResponse(exercise));
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"Message\": \"Error fetching exercise.\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }

    public ResponseEntity<?> deleteById(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                if (id < 0){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Message\": \"Id must be positive.\"}");
                }

                // fetch user id
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDAOImpl userDAO = new UserDAOImpl();
                int userId = userDAO.getUserByUsername(userDetails.getUsername()).getId();

                // check if exercise is created by the author of the request
                ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl();
                if (!exerciseDAO.isCreatedBy(id, userId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"You can not delete this exercise. You are not the author.\"}");
                }


                // delete exercise
                if (exerciseDAO.deleteExercise(id)) {
                    return ResponseEntity.ok().body("{\"Message\": \"Deleted successfully.\"}");
                }
                else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Message\": \"Exercise not found.\"}");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"Message\": \"Error fetching exercise.\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }
}
