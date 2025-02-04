package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Category;

import java.util.List;

public interface CategoryDAO {
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    void createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(int id);
}