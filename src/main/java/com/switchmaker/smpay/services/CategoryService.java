/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 11:41
 * @Date: 15/01/2024
 */
package com.switchmaker.smpay.services;

import com.switchmaker.smpay.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category saveCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(UUID id);
    Category updateCategory(UUID id, Category category);
    void deleteCategory(UUID id);
}
