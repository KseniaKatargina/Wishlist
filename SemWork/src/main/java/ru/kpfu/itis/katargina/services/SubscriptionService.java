package ru.kpfu.itis.katargina.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.dto.SubscriptionDto;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Subscription;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.repositories.CategoryRepository;
import ru.kpfu.itis.katargina.repositories.SubscriptionRepository;
import ru.kpfu.itis.katargina.repositories.UserRepository;
import ru.kpfu.itis.katargina.utils.DtoConverter;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public SubscriptionDto save(SubscriptionDto subscription) {
        List<Category> categoryDtoList = categoryRepository.findAllByIds(subscription.getCategoriesIds());
        User user = userRepository.findByEmail(subscription.getUserEmail());
        Subscription subscriptionToSave = DtoConverter.fromDto(subscription, Subscription.class);
        subscriptionToSave.setId(subscription.getId());
        subscriptionToSave.setCategories(DtoConverter.fromDtoList(categoryDtoList, Category.class));
        subscriptionToSave.setUser(user);
        SubscriptionDto subscriptionDto = SubscriptionDto.from(subscriptionRepository.save(subscriptionToSave));
        for (Category category : categoryDtoList) {
            category.getSubscriptions().add(subscriptionToSave);
            categoryRepository.save(category);
        }
        return subscriptionDto;
    }

    public SubscriptionDto getUsersSubs(String email) {
        Subscription subscription = subscriptionRepository.findByUserEmail(email);
        return DtoConverter.toDto(subscription, SubscriptionDto.class);
    }

    public void unsubscribeCategory(String email, Long categoryId) {
        Subscription subscription = subscriptionRepository.findByUserEmail(email);
        if (subscription != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isPresent()) {
                subscription.getCategories().remove(category.get());
                category.get().getSubscriptions().remove(subscription);
                SubscriptionDto subscriptionDto = DtoConverter.toDto(subscription, SubscriptionDto.class);
                subscriptionDto.setUserEmail(email);
                subscriptionDto.setCategoriesIds(subscription.getCategories().stream().map(Category::getId).toList());
                categoryRepository.save(category.get());
            }
        }
    }
}
