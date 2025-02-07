package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.CategoryDAOImpl;
import it.unical.web.backend.persistence.dao.DAOInterface.CategoryDAO;
import it.unical.web.backend.persistence.dao.DAOInterface.TagDAO;
import it.unical.web.backend.persistence.dao.TagDAOImpl;
import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.persistence.model.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryDAOImpl categoryDAO=new CategoryDAOImpl();
    private final TagDAOImpl tagDAO=new TagDAOImpl();


    // Metodo per recuperare tutte le categorie con i relativi tag
    public List<Category> getAllCategoriesWithTags() {
        List<Category> categories = categoryDAO.getAllCategories();

        for (Category category : categories) {
            // Recupera i tag associati alla categoria
            List<Tag> tags = tagDAO.getTagsByCategoryId(category.getId());

            category.setTags(tags);
        }

        return categories;
    }
}