package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Tag;

import java.util.List;

public interface TagDAO {
    Tag getTagById(int id);
    List<Tag> getAllTagsByCategory(int categoryId);
    void createTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTag(int id);
}