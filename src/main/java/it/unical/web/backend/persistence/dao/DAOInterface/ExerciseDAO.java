package it.unical.web.backend.persistence.dao.DAOInterface;

//TODO: Aggiungere eliminazione di un elemento

import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.WorkoutPlan;
import it.unical.web.backend.persistence.model.WorkoutPlanAndExercise;

import java.util.List;

public interface ExerciseDAO {
    Exercise getExerciseById(int id, int idUser);
    List<Exercise> getAllExercisesByUser(int userId);
    void createExercise(Exercise exercise);
    void updateExercise(Exercise exercise);
    boolean deleteExercise(int id);
}
