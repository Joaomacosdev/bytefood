package br.com.bytefood.category.service;

import br.com.bytefood.category.dtos.CategoryDTO;
import br.com.bytefood.response.Response;

import java.util.List;

public interface CategoryService {
    Response<CategoryDTO> addCategory(CategoryDTO categoryDTO);
    Response<CategoryDTO> updateCategory(CategoryDTO categoryDTO);
    Response<CategoryDTO> getCategoryById(Long id);
    Response<List<CategoryDTO>> getAllCategories();
    Response<?> deleteCategory(Long id);

}
