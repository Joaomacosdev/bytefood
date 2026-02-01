package br.com.bytefood.category.service;

import br.com.bytefood.category.dtos.CategoryDTO;
import br.com.bytefood.category.entity.Category;
import br.com.bytefood.category.repository.CategoryRepository;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<CategoryDTO> addCategory(CategoryDTO categoryDTO) {
        log.info("Inside addCategory()");

        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(category);


        return Response.<CategoryDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Category added successfully")
                .build();
    }

    @Override
    public Response<CategoryDTO> updateCategory(CategoryDTO categoryDTO) {

        log.info("Inside updateCategory()");

        Category category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        if (categoryDTO.getName() != null && categoryDTO.getName().isEmpty()) category.setName(category.getName());
        if (categoryDTO.getDescription() != null && categoryDTO.getDescription().isEmpty()) category.setDescription(category.getDescription());

        categoryRepository.save(category);

        return  Response.<CategoryDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Category added successfully")
                .build();
    }

    @Override
    public Response<CategoryDTO> getCategoryById(Long id) {

        log.info("Inside getCategory()");

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return  Response.<CategoryDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Category get by id successfully")
                .data(categoryDTO)
                .build();
    }

    @Override
    public Response<List<CategoryDTO>> getAllCategories() {

        log.info("Inside getAllCategories()");

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        return Response.<List<CategoryDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All categories retrieved successfully")
                .data(categoryDTOS)
                .build();
    }

    @Override
    public Response<?> deleteCategory(Long id) {

        log.info("Inside deleteCategory()");

        if (!categoryRepository.existsById(id)){
            throw new NotFoundException("Category Not Found");
        }

        categoryRepository.deleteById(id);

        return  Response.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Category deleted successfully")
                .build();
    }
}
