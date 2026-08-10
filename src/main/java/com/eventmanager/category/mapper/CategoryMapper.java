package com.eventmanager.category.mapper;

import com.eventmanager.category.dto.response.CategoryResponse;
import com.eventmanager.category.entity.Category;
import org.springframework.stereotype.Component;
import com.eventmanager.category.dto.request.CreateCategoryRequest;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse (Category category){

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }

    public Category toEntity (CreateCategoryRequest request){

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return category;
    }
}
