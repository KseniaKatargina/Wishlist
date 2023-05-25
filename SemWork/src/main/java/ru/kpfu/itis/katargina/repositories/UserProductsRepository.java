package ru.kpfu.itis.katargina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.katargina.models.UserProduct;

import java.util.List;

public interface UserProductsRepository extends JpaRepository<UserProduct, Long> {
    @Query(value = "select up from UserProduct up where up.user.id = :userId and up.product.id = :productId")
    UserProduct findAllByUserIdAndProductId(Long userId, Long productId);
}
