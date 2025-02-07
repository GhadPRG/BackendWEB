package it.unical.web.backend.service;

import it.unical.web.backend.controller.DTO.NoteDTO;
import it.unical.web.backend.persistence.dao.NoteDAOImpl;
import it.unical.web.backend.persistence.model.Note;

import java.util.List;

public class NoteService {

    public List<NoteDTO> getNotes() {
        NoteDAOImpl noteDAO = new NoteDAOImpl();
        UserService userService = new UserService();
        NoteDTO noteDTO = new NoteDTO();
        return noteDTO.getNoteDTOS(noteDAO.getAllNotesByUser(userService.getCurrentUserIdByUsername()));
    }

    public Note addNote(Note note) {
        NoteDAOImpl noteDAO = new NoteDAOImpl();
        return noteDAO.createNote(note);
    }

    public void updateNote(Note note) {
        NoteDAOImpl noteDAO = new NoteDAOImpl();
        noteDAO.updateNote(note);
    }

    public void deleteNote(int noteId) {
        NoteDAOImpl noteDAO = new NoteDAOImpl();
        noteDAO.deleteNote(noteId);
    }

}
