package com.eventmanager.category.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCategoryRequest {

    @NotBlank
    @Size(max = 100, min = 2)
    private String name;

    @NotBlank
    @Size(max = 2000)
    private String description;

}
