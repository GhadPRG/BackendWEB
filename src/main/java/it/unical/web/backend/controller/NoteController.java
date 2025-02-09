package it.unical.web.backend.controller;

import it.unical.web.backend.controller.DTO.NoteDTO;
import it.unical.web.backend.persistence.model.Note;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.service.NoteService;
import it.unical.web.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: Refactor codice duplichello
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NoteDTO>> getNotes() {

        NoteService noteService = new NoteService();
        return ResponseEntity.ok(noteService.getNotes());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Note> createNote(@RequestBody Map<String, Object> payload) {

        NoteService noteService = new NoteService();
        Note note = new Note();
        note.setTitle((String) payload.get("title"));
        note.setContent((String) payload.get("content"));
        //note.setCreatedAt((LocalDateTime) payload.get("created_at")); Lo ignoro, va aggiunto in caso di necessità

        List<Integer> tags = (List<Integer>) payload.get("tags");
        List<Tag> tagforNote = new ArrayList<>();
        for(Integer t : tags) {
            Tag tag = new Tag();
            tag.setId(t);
            tagforNote.add(tag);
        }
        note.setTags(tagforNote);

        UserService userService = new UserService();
        note.setUser(userService.getUserById(userService.getCurrentUserIdByUsername()));

        return ResponseEntity.ok().body(noteService.addNote(note));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteNote(@RequestParam int id) {
        NoteService noteService = new NoteService();
        noteService.deleteNote(id);
        return ResponseEntity.ok().body("Vamo Raga note Deleted");
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateNote(@RequestBody Map<String, Object> payload) {
        NoteService noteService = new NoteService();
        Note note = new Note();
        note.setId((int)payload.get("id"));
        note.setTitle((String) payload.get("title"));
        note.setContent((String) payload.get("content"));
        List<Integer> tags = (List<Integer>) payload.get("tags");
        List<Tag> tagforNote = new ArrayList<>();
        for(Integer t : tags) {
            Tag tag = new Tag();
            tag.setId(t);
            tagforNote.add(tag);
        }
        note.setTags(tagforNote);
        UserService userService = new UserService();
        note.setUser(userService.getUserById(userService.getCurrentUserIdByUsername()));
        noteService.updateNote(note);
        return ResponseEntity.ok().body("Vamo Raga note Updated");
    }

}
