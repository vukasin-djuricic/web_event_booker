package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uma_gym.web_event_booker.model.Category;
import org.uma_gym.web_event_booker.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return this.categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        // Ovde ide poslovna logika, npr. provera da li već postoji kategorija sa tim imenom
        return this.categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        // Ovde ide poslovna logika, npr. provera da li postoji događaj sa ovom kategorijom
        this.categoryRepository.deleteById(id);
    }
}