package ru.kpfu.itis.katargina.dto;

import lombok.*;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.models.Wishlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private List<Long> productsIds;
    private List<Long> subscriptionsIds = new ArrayList<>();


    public CategoryDto(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productsIds(category.getProducts() == null ? new ArrayList() : category.getProducts().stream().map(Product::getId).collect(Collectors.toList()))
                .subscriptionsIds(category.getSubscriptions() == null ? new ArrayList() : category.getSubscriptions().stream().map(Subscription::getId).collect(Collectors.toList()))
                .build();
    }

    public static Category to(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .products(categoryDto.getProductsIds().stream().map(id -> Product.builder().id(id).build()).collect(Collectors.toList()))
                .subscriptions(categoryDto.getSubscriptionsIds().stream().map(id -> Subscription.builder().id(id).build()).collect(Collectors.toList()))
                .build();
    }

    public static List<CategoryDto> from(List<Category> categories) {
        return categories.stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
    }

    public static List<Category> toList(List<CategoryDto> categories) {
        return categories.stream()
                .map(CategoryDto::to)
                .collect(Collectors.toList());
    }
}