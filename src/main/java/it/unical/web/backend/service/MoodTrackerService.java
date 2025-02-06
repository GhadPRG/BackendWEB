package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.DAOInterface.MoodTrackerDAO;
import it.unical.web.backend.persistence.model.MoodTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodTrackerService {

    private final MoodTrackerDAO moodTrackerDAO;

    @Autowired
    public MoodTrackerService(MoodTrackerDAO moodTrackerDAO) {
        this.moodTrackerDAO = moodTrackerDAO;
    }


    public MoodTracker getMoodById(int id) {
        return moodTrackerDAO.getMoodTrackerById(id);
    }


    public List<MoodTracker> getAllMoodsByUser(int userId) {
        return moodTrackerDAO.getAllMoodTrackersByUser(userId);
    }


    public void createMood(MoodTracker moodTracker) {
        if (moodTracker.getMoodLevel() < 1 || moodTracker.getMoodLevel() > 10) {
            throw new IllegalArgumentException("Mood level must be between 1 and 10");
        }
        moodTrackerDAO.createMoodTracker(moodTracker);
    }


    public void updateMood(MoodTracker moodTracker) {
        if (moodTrackerDAO.getMoodTrackerById(moodTracker.getId()) == null) {
            throw new IllegalArgumentException("MoodTracker not found");
        }
        moodTrackerDAO.updateMoodTracker(moodTracker);
    }


    public int deleteMood(int id) {
        return moodTrackerDAO.deleteMoodTracker(id);
    }
}
