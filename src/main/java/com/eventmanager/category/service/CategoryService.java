package com.eventmanager.category.service;


import com.eventmanager.category.dto.request.CreateCategoryRequest;
import com.eventmanager.category.dto.response.CategoryResponse;
import com.eventmanager.category.entity.Category;
import com.eventmanager.category.mapper.CategoryMapper;
import com.eventmanager.category.repository.CategoryRepository;
import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.event.dto.response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse create(CreateCategoryRequest request) {

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> findAll() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse update(
            Long id,
            CreateCategoryRequest request
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        ));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        ));

        categoryRepository.delete(category);
    }
}
