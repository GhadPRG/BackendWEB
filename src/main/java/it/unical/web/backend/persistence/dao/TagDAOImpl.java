package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.TagDAO;
import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.persistence.model.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAOImpl implements TagDAO {
    private Connection connection= DatabaseConnection.getConnection();

    @Override
    public Tag getTagById(int id) {
        String query = "SELECT * FROM tags WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));

                // Fetch the category of the tag
                Category category = new Category();
                category.setId(rs.getInt("category_id")); // Assume the category is already fetched elsewhere
                tag.setCategory(category);

                return tag;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tag> getAllTagsByCategory(int categoryId) {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT * FROM tags WHERE category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));

                // Fetch the category of the tag
                Category category = new Category();
                category.setId(categoryId); // Assume the category is already fetched elsewhere
                tag.setCategory(category);

                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    @Override
    public void createTag(Tag tag) {
        String query = "INSERT INTO tags (category_id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, tag.getCategory().getId());
            stmt.setString(2, tag.getName());
            stmt.setString(3, tag.getDescription());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                tag.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTag(Tag tag) {
        String query = "UPDATE tags SET category_id = ?, name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tag.getCategory().getId());
            stmt.setString(2, tag.getName());
            stmt.setString(3, tag.getDescription());
            stmt.setInt(4, tag.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTag(int id) {
        String query = "DELETE FROM tags WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}