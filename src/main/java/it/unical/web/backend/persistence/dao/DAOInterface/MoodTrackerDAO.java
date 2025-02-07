package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.MoodTracker;

import java.util.List;

public interface MoodTrackerDAO {
    MoodTracker getMoodTrackerById(int id);
    List<MoodTracker> getAllMoodTrackersByUser(int userId);
    void createMoodTracker(MoodTracker moodTracker);
    void updateMoodTracker(MoodTracker moodTracker);
    int deleteMoodTracker(int id);
}