package ru.kpfu.itis.katargina.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.models.UserProduct;
import ru.kpfu.itis.katargina.repositories.UserProductsRepository;
import ru.kpfu.itis.katargina.utils.DtoConverter;

import java.util.List;

@Service
public class UserProductsService {
    @Autowired
    private UserProductsRepository userProductsRepository;

    public void save(UserDto user, ProductDto product, boolean isSeen){
        UserProduct userProduct = UserProduct.builder()
                .user(UserDto.to(user))
                .product(ProductDto.to(product))
                .seen(isSeen)
                .build();
        userProductsRepository.save(userProduct);

    }

    public UserProduct findByUserAndProduct(Long userId, Long productId){
        return userProductsRepository.findAllByUserIdAndProductId(userId, productId);
    }

    public void delete(UserDto user, ProductDto product){
        UserProduct userProduct = findByUserAndProduct(user.getId(), product.getId());
        userProductsRepository.delete(userProduct);
    }

    public void markAsSeen(Long userId, Long productId) {
        UserProduct userProduct = userProductsRepository.findAllByUserIdAndProductId(userId, productId);
        userProduct.setSeen(true);
        userProductsRepository.save(userProduct);
    }
}
