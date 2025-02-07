package it.unical.web.backend.persistence.model;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Note {
    private int id;
    private User user;
    private String title;
    private String content;
    private LocalDate createdAt;
    private List<Tag> tags;


    public List<Integer> tagsToInt() {
        List<Integer> tagsID = new ArrayList<>();
        for (Tag tag : this.tags) {
            tagsID.add(tag.getId());
        }
        return tagsID;
    }

}