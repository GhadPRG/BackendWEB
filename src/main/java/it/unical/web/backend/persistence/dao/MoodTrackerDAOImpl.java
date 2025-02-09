package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.MoodTrackerDAO;
import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.persistence.model.MoodTracker;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.persistence.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MoodTrackerDAOImpl implements MoodTrackerDAO {
    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public MoodTracker getMoodTrackerById(int id) {
        String query = "SELECT * FROM mood_tracker WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MoodTracker moodTracker = new MoodTracker();
                moodTracker.setId(rs.getInt("id"));
                moodTracker.setMoodLevel(rs.getInt("mood_level"));
                moodTracker.setMoodDate(rs.getDate("mood_date").toLocalDate());
                moodTracker.setNotes(rs.getString("notes"));

                // Fetch the user who recorded the mood
                User user = new User();
                user.setId(rs.getInt("user_id")); // Assume the user is already fetched elsewhere
                moodTracker.setUser(user);

                // Fetch tags associated with this mood record
                moodTracker.setTags(getTagsByEntityIdAndType(id, "mood"));

                return moodTracker;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MoodTracker> getAllMoodTrackersByUser(int userId) {
        List<MoodTracker> moodTrackers = new ArrayList<>();
        String query = "SELECT * FROM mood_tracker WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MoodTracker moodTracker = new MoodTracker();
                moodTracker.setId(rs.getInt("id"));
                moodTracker.setMoodLevel(rs.getInt("mood_level"));
                moodTracker.setMoodDate(rs.getDate("mood_date").toLocalDate());
                moodTracker.setNotes(rs.getString("notes"));

                // Fetch the user who recorded the mood
                User user = new User();
                user.setId(userId); // Assume the user is already fetched elsewhere
                moodTracker.setUser(user);

                // Fetch tags associated with this mood record
                moodTracker.setTags(getTagsByEntityIdAndType(moodTracker.getId(), "mood"));

                moodTrackers.add(moodTracker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moodTrackers;
    }

    @Override
    public void createMoodTracker(MoodTracker moodTracker) {
        String query = "INSERT INTO mood_tracker (user_id, mood_level, mood_date, notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setInt(1, moodTracker.getUser().getId());
            stmt.setInt(2, moodTracker.getMoodLevel());
            stmt.setDate(3, Date.valueOf(moodTracker.getMoodDate()));
            stmt.setString(4, moodTracker.getNotes());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                moodTracker.setId(rs.getInt(1));
            }

            // Add tags to the mood record
            for (Tag tag : moodTracker.getTags()) {
                addTagToEntity(moodTracker.getId(), "mood", tag.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMoodTracker(MoodTracker moodTracker) {
        String query = "UPDATE mood_tracker SET user_id = ?, mood_level = ?, mood_date = ?, notes = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, moodTracker.getUser().getId());
            stmt.setInt(2, moodTracker.getMoodLevel());
            stmt.setDate(3, Date.valueOf(moodTracker.getMoodDate()));
            stmt.setString(4, moodTracker.getNotes());
            stmt.setInt(5, moodTracker.getId());
            stmt.executeUpdate();

            // Update tags for the mood record
            removeTagsFromEntity(moodTracker.getId(), "mood");
            for (Tag tag : moodTracker.getTags()) {
                addTagToEntity(moodTracker.getId(), "mood", tag.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int deleteMoodTracker(int id) {
        String query = "DELETE FROM mood_tracker WHERE id = ?";
        int updateRet = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            updateRet = stmt.executeUpdate();

            // Remove tags associated with the mood record
            removeTagsFromEntity(id, "mood");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateRet;
    }

    private List<Tag> getTagsByEntityIdAndType(int entityId, String entityType) {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT t.* FROM tags t " +
                "JOIN entity_tags et ON t.id = et.tag_id " +
                "WHERE et.entity_id = ? AND et.entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entityId);
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

    private void addTagToEntity(int entityId, String entityType, int tagId) {
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

    private void removeTagsFromEntity(int entityId, String entityType) {
        String query = "DELETE FROM entity_tags WHERE entity_id = ? AND entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entityId);
            stmt.setString(2, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}