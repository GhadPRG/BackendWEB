package it.unical.web.backend.controller;

import it.unical.web.backend.persistence.model.MoodTracker;
import it.unical.web.backend.service.MoodTrackerService;
import it.unical.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
public class MoodTrackerController {
    private final MoodTrackerService moodTrackerService;
    private final UserService userService;

    @Autowired
    public MoodTrackerController(MoodTrackerService moodTrackerService, UserService userService) {
        this.moodTrackerService = moodTrackerService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MoodTracker>> getUserMoods() {
        int userId = userService.getCurrentUserIdByUsername();
        List<MoodTracker> moods = moodTrackerService.getAllMoodsByUser(userId);

        System.out.println("Current Moods Found");
        System.out.println(moods);
        return new ResponseEntity<>(moods, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addMood(@RequestBody Map<String, Object> payload) {
        System.out.print("Trying to Add This Mood");
        System.out.println(payload);

        int userId = userService.getCurrentUserIdByUsername();
        int moodLevel = (int) payload.get("moodLevel");
        String note = (String) payload.get("note");

        MoodTracker moodTracker = new MoodTracker();
        moodTracker.setUser(userService.getUserById(userId));
        moodTracker.setMoodLevel(moodLevel);
        moodTracker.setNotes(note);
        moodTracker.setMoodDate(LocalDate.now());

        moodTrackerService.createMood(moodTracker);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @DeleteMapping
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<String> deleteMood(@RequestParam int id) {
//        if (moodTrackerService.deleteMood(id) > 0) {
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
