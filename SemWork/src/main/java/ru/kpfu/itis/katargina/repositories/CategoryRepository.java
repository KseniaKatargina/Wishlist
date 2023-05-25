package ru.kpfu.itis.katargina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Subscription;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Transactional
    @Query(value = "insert into products_categories(products_id, categories_id) values (:productId, :categoryId)", nativeQuery = true)
    void addCategoryProduct(Long productId, Long categoryId);

    @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :productID")
    List<Category> findByProductId(Long productID);

    Category findByName(String name);

    @Query(value = "select c from Category c where c.id in :ids")
    List<Category> findAllByIds(List<Long> ids);

    @Query("SELECT c FROM Category c JOIN c.subscriptions s WHERE s = :sub")
    List<Category> findAllSubscribedCategories(Subscription sub);

}
