package com.ucbcba.demo.services;

import com.ucbcba.demo.Entities.Category;

public interface CategoryService {
    Iterable<Category> listAllCategories();

    void saveCategory(Category category);

    Category getCategory(Integer id);

    void deleteCategory(Integer id);
}
