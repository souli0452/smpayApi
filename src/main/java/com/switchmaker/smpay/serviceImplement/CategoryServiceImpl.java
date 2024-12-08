/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 11:47
 * @Date: 15/01/2024
 */
package com.switchmaker.smpay.serviceImplement;

import com.switchmaker.smpay.entities.Category;
import com.switchmaker.smpay.repository.CategoryRepository;
import com.switchmaker.smpay.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category category) {
        category.setId(UUID.randomUUID());
        category.setWording(category.getWording().toUpperCase());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "wording"));
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
        categoryRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Id " + id+" invalid"));
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
