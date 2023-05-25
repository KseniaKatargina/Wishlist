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
public class WishlistDto {
    private Long id;

    private String title;

    private String description;

    private Long userId;

    private List<Long> productIds = new ArrayList<>();

    public WishlistDto(String title, String description, Long userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public static WishlistDto from(Wishlist wishlist) {
        return WishlistDto.builder()
                .id(wishlist.getId())
                .title(wishlist.getTitle())
                .description(wishlist.getDescription())
                .userId(wishlist.getUser().getId())
                .productIds(wishlist.getProducts() == null ? new ArrayList() : wishlist.getProducts().stream().map(Product::getId).collect(Collectors.toList()))
                .build();
    }

    public static Wishlist to(WishlistDto wishlistDto) {
        return Wishlist.builder()
                .id(wishlistDto.getId())
                .title(wishlistDto.getTitle())
                .description(wishlistDto.getDescription())
                .user(User.builder().id(wishlistDto.getUserId()).build())
                .products(wishlistDto.getProductIds().stream().map(id -> Product.builder().id(id).build()).collect(Collectors.toList()))
                .build();
    }

    public static List<WishlistDto> from(List<Wishlist> wishlists) {
        return wishlists.stream()
                .map(WishlistDto::from)
                .collect(Collectors.toList());
    }

    public static List<Wishlist> toList(List<WishlistDto> wishlists) {
        return wishlists.stream()
                .map(WishlistDto::to)
                .collect(Collectors.toList());
    }
}
