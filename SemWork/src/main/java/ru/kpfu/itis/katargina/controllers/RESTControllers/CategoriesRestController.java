package ru.kpfu.itis.katargina.controllers.RESTControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.services.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriesRestController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getAllCategories();
    }

}
