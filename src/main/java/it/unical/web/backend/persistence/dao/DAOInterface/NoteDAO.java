package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Note;

import java.util.List;

public interface NoteDAO {
    Note getNoteById(long id);
    List<Note> getAllNotesByUser(int userId);
    Note createNote(Note note);
    void updateNote(Note note);
    void deleteNote(int id);
}