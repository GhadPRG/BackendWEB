package it.unical.web.backend.controller;

import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Endpoint per ottenere tutte le categorie con i relativi tag
    @GetMapping
    public List<Category> getAllCategoriesWithTags() {
        return categoryService.getAllCategoriesWithTags();
    }
}