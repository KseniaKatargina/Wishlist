package ru.kpfu.itis.katargina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.models.Wishlist;
import ru.kpfu.itis.katargina.utils.Role;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank
    @NotNull
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}", message = "Пароль должен содержать цифры, строчные и заглавные латинские буквы, а также не менее 8 знаков")
    private String password;

    @NotBlank
    @NotNull
    private String birthday;

    private int years;

    @NotNull
    @Email(message = "Неверный email")
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    private List<Long> wishlistIds;

    private Role role;

    private Long subscriptionId;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .birthday(user.getBirthday())
                .years(user.getYears())
                .role(user.getRole())
                .subscriptionId(user.getSubscription() == null ? null : user.getSubscription().getId())
                .wishlistIds(user.getWishlists() == null ? new ArrayList<>() : user.getWishlists().stream().map(Wishlist::getId).collect(Collectors.toList()))
                .build();
    }

    public static User to(UserDto user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthday(user.getBirthday())
                .years(user.getYears())
                .role(user.getRole())
                .password(user.getPassword())
                .subscription(Subscription.builder().id(user.getSubscriptionId()).build())
                .wishlists(user.getWishlistIds() == null ? new ArrayList<>() : user.getWishlistIds().stream().map(id -> Wishlist.builder().id(id).build()).collect(Collectors.toList()))
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    public static List<User> toList(List<UserDto> users) {
        return users.stream()
                .map(UserDto::to)
                .collect(Collectors.toList());
    }
}
