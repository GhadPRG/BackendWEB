package it.unical.web.backend.controller.DTO;

import it.unical.web.backend.persistence.model.Note;
import it.unical.web.backend.persistence.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class NoteDTO {
    private int id;
    private User user;
    private String title;
    private String content;
    private LocalDate createdAt;
    private List<Integer> tags;


    public NoteDTO(Note note) {
        this.id = note.getId();
        this.user = note.getUser();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();
        this.tags = note.tagsToInt();
    }

    public List<NoteDTO> getNoteDTOS(List<Note> note) {
        List<NoteDTO> noteDTOS = new ArrayList<>();
        for (Note note2 : note) {
            noteDTOS.add(new NoteDTO(note2));
        }
        return noteDTOS;
    }
}
