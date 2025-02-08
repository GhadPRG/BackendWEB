package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.TagDAO;
import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.persistence.model.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAOImpl implements TagDAO {
    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public List<Tag> getTagsByCategoryId(int categoryId) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.id AS tag_id, t.name AS tag_name, t.description AS tag_description, " +
                "c.id AS category_id, c.name AS category_name, c.description AS category_description, c.color AS category_color " +
                "FROM tags t " +
                "JOIN categories c ON t.category_id = c.id " +
                "WHERE t.category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tag tag = new Tag();
                    tag.setId(rs.getInt("tag_id"));
                    tag.setName(rs.getString("tag_name"));
                    tag.setDescription(rs.getString("tag_description"));

                    // Crea l'oggetto Category
                    Category category = new Category();
                    category.setId(rs.getInt("category_id"));
                    category.setName(rs.getString("category_name"));
                    category.setDescription(rs.getString("category_description"));
                    category.setColor(rs.getString("category_color"));

                    // Associa la categoria al tag
                    //tag.setCategory(category);

                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dei tag", e);
        }
        return tags;
    }

    public void addTagToEntity(int entityId, String entityType, int tagId) {
        String query = "INSERT INTO entity_tags (entity_id, entity_type, tag_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entityId);
            stmt.setString(2, entityType);
            stmt.setInt(3, tagId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTagsFromEntity(int entityId, String entityType) {
        String query = "DELETE FROM entity_tags WHERE entity_id = ? AND entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entityId);
            stmt.setString(2, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tag getTagById(int id) {
        String sql = "SELECT t.id AS tag_id, t.name AS tag_name, t.description AS tag_description, " +
                "c.id AS category_id, c.name AS category_name, c.description AS category_description, c.color AS category_color " +
                "FROM tags t " +
                "JOIN categories c ON t.category_id = c.id " +
                "WHERE t.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tag tag = new Tag();
                    tag.setId(rs.getInt("tag_id"));
                    tag.setName(rs.getString("tag_name"));
                    tag.setDescription(rs.getString("tag_description"));

                    // Crea l'oggetto Category
                    Category category = new Category();
                    category.setId(rs.getInt("category_id"));
                    category.setName(rs.getString("category_name"));
                    category.setDescription(rs.getString("category_description"));
                    category.setColor(rs.getString("category_color"));

                    // Associa la categoria al tag
                    tag.setCategory(category);

                    return tag;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero del tag", e);
        }
        return null;
    }

    @Override
    public void addTag(Tag tag) {
        String sql = "INSERT INTO tags (category_id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, tag.getCategory().getId());
            stmt.setString(2, tag.getName());
            stmt.setString(3, tag.getDescription());
            stmt.executeUpdate();

            // Recupera l'ID generato automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tag.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'inserimento del tag", e);
        }
    }

    @Override
    public void updateTag(Tag tag) {
        String sql = "UPDATE tags SET category_id = ?, name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tag.getCategory().getId());
            stmt.setString(2, tag.getName());
            stmt.setString(3, tag.getDescription());
            stmt.setInt(4, tag.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento del tag", e);
        }
    }

    @Override
    public void deleteTag(int id) {
        String sql = "DELETE FROM tags WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione del tag", e);
        }
    }

    public List<Tag> getTagsByEntityIdAndType(long entityId, String entityType) {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT t.* FROM tags t " +
                "JOIN entity_tags et ON t.id = et.tag_id " +
                "WHERE et.entity_id = ? AND et.entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, entityId);
            stmt.setString(2, entityType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));

                // Fetch the category of the tag
                Category category = new Category();
                category.setId(rs.getInt("category_id")); // Assume the category is already fetched elsewhere
                tag.setCategory(category);

                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

}