package ru.kpfu.itis.katargina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.models.Wishlist;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAllByUserId(Long id);

    Wishlist findByIdAndUser(Long listID, User user);
    Wishlist findWishlistById(Long id);

    @Query("SELECT p FROM Product p JOIN p.wishlists w WHERE w.id = :wishlistId")
    List<Product> findAllProductsByWishlistId(@Param("wishlistId") Long wishlistId);
}
