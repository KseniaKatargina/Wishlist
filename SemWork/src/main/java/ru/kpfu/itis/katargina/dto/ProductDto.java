package ru.kpfu.itis.katargina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.katargina.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String img;
    private String text;
    private List<Long> wishlistsIds = new ArrayList<>();
    private List<String> categoriesNames = new ArrayList<>();

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .img(product.getImg())
                .text(product.getText())
                .wishlistsIds(product.getWishlists() == null ? new ArrayList() : product.getWishlists().stream().map(Wishlist::getId).collect(Collectors.toList()))
                .categoriesNames(product.getCategories() == null ? new ArrayList() : product.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .build();
    }

    public static Product to(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .img(productDto.getImg())
                .text(productDto.getText())
                .wishlists(productDto.getWishlistsIds().stream().map(id -> Wishlist.builder().id(id).build()).collect(Collectors.toList()))
                .categories(productDto.getCategoriesNames().stream().map(name -> Category.builder().name(name).build()).collect(Collectors.toList()))
                .build();
    }

    public static List<ProductDto> from(List<Product> products) {
        return products.stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }

    public static List<Product> toList(List<ProductDto> products) {
        return products.stream()
                .map(ProductDto::to)
                .collect(Collectors.toList());
    }
}
