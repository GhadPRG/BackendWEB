package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Tag;

import java.util.List;

public interface TagDAO {
    List<Tag> getTagsByCategoryId(int categoryId);
    Tag getTagById(int id);
    void addTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTag(int id);
}