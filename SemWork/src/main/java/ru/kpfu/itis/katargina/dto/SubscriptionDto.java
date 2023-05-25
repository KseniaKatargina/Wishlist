package ru.kpfu.itis.katargina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private String name;
    private List<Long> categoriesIds = new ArrayList<>();
    private String userEmail;

    public static SubscriptionDto from(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .userEmail(subscription.getUser().getEmail())
                .categoriesIds(subscription.getCategories() == null ? new ArrayList() : subscription.getCategories().stream().map(Category::getId).collect(Collectors.toList()))
                .build();
    }

    public static Subscription to(SubscriptionDto subscriptionDto) {
        return Subscription.builder()
                .id(subscriptionDto.getId())
                .name(subscriptionDto.getName())
                .user(User.builder().email(subscriptionDto.getUserEmail()).build())
                .categories(subscriptionDto.getCategoriesIds().stream().map(id -> Category.builder().id(id).build()).collect(Collectors.toList()))
                .build();
    }

    public static List<SubscriptionDto> from(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(SubscriptionDto::from)
                .collect(Collectors.toList());
    }

    public static List<Subscription> toList(List<SubscriptionDto> subscriptions) {
        return subscriptions.stream()
                .map(SubscriptionDto::to)
                .collect(Collectors.toList());
    }
}
