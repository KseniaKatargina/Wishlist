package ru.kpfu.itis.katargina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.katargina.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    @Query(value = "SELECT p.*" +
            "FROM products p " +
            "INNER JOIN wishlists_entry we ON p.id = we.product_id " +
            "WHERE we.wishlist_id = :wishlistId", nativeQuery = true)
    List<Product> findAllByWishListId(Long wishlistId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM wishlists_entry we WHERE we.product_id = :prodID AND we.wishlist_id = :wishListID", nativeQuery = true)
    void deleteFromEntryByID(Long prodID, Long wishListID);


    @Query(value = "SELECT * from products where products.id in (select products_id from products_categories where categories_id in :categoryIds)", nativeQuery = true)
    List<Product> getProductsByCategories(@Param("categoryIds") List<Integer> categoryIds);

    @Query("SELECT p FROM Product p WHERE p.id NOT IN " +
            "(SELECT up.product.id FROM UserProduct up WHERE up.user.id = :userId AND up.seen = true)")
    List<Product> getUnseenProducts(Long userId);
}
