package ru.kpfu.itis.katargina.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.dto.SubscriptionDto;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.repositories.CategoryRepository;
import ru.kpfu.itis.katargina.utils.DtoConverter;

import java.util.ArrayList;
import java.util.List;

import static ru.kpfu.itis.katargina.utils.DtoConverter.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return toDtoList(categoryRepository.findAll(), CategoryDto.class);
    }

    public List<CategoryDto> getAllSubscribedCategories(SubscriptionDto subscriptionDto) {
        return toDtoList(categoryRepository.findAllSubscribedCategories(fromDto(subscriptionDto, Subscription.class)), CategoryDto.class);
    }

    public void addAllCategories(List<CategoryDto> categories) {
        categoryRepository.saveAll((fromDtoList(categories, Category.class)));
    }

    public List<CategoryDto> findByProductId(Long productID){
        return toDtoList(categoryRepository.findByProductId(productID), CategoryDto.class);
    }

    public CategoryDto findByName(String name) {
        return toDto(categoryRepository.findByName(name), CategoryDto.class);
    }

    public void addAllCategoriesByProductId(List<CategoryDto> categories, Long productId) {
        for (CategoryDto category : categories) {
            Long categoryId;
            try {
                categoryId = findByName(category.getName()).getId();
            } catch (NullPointerException exc) {
                categoryId = categoryRepository.save(fromDto(category, Category.class)).getId();
            }
            categoryRepository.addCategoryProduct(productId, categoryId);
        }
    }

}
