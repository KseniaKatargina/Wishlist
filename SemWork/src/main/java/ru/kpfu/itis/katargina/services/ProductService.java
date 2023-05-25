package ru.kpfu.itis.katargina.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.exceptions.NotFoundException;
import ru.kpfu.itis.katargina.models.Category;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.Wishlist;
import ru.kpfu.itis.katargina.repositories.CategoryRepository;
import ru.kpfu.itis.katargina.repositories.ProductRepository;
import ru.kpfu.itis.katargina.repositories.WishlistRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.kpfu.itis.katargina.utils.DtoConverter.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    private final WishlistRepository wishlistRepository;

    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts() {
        return toDtoList(productRepository.findAll(), ProductDto.class);
    }

    public List<ProductDto> getUnSeenProducts(Long userId) {
        List<Product> products = productRepository.getUnseenProducts(userId);
        List<ProductDto> productDtos;
        productDtos = products.stream().map(product -> ProductDto.builder()
                .id(product.getId())
                .text(product.getText())
                .img(product.getImg())
                .categoriesNames(product.getCategories().stream().map(Category::getName).toList())
                .wishlistsIds(product.getWishlists().stream().map(Wishlist::getId).toList())
                .build()).toList();
        return productDtos;
    }

    public void removeProductFromWishlist(Long prodID, Long listID) {
        Wishlist wishlist = wishlistRepository.findById(listID)
                .orElseThrow(() -> new NotFoundException("Wishlist not found with id " + listID));
        Product product = productRepository.findProductById(prodID);
        wishlist.getProducts().remove(product);
        productRepository.deleteFromEntryByID(prodID, listID);
    }

    public List<ProductDto> getAllByWishListId(Long wishlistId) {
        return toDtoList(productRepository.findAllByWishListId(wishlistId), ProductDto.class);
    }

    public List<ProductDto> getProductsByCategories(List<Integer> selectedCategories) {
        if (selectedCategories.isEmpty()) {
            return getAllProducts();
        }
        return toDtoList(productRepository.getProductsByCategories(selectedCategories), ProductDto.class);
    }

    public void deleteProduct(ProductDto product) {
        productRepository.delete(fromDto(product, Product.class));
    }

    public ProductDto getProductById(Long productId) {
        return toDto(productRepository.findProductById(productId), ProductDto.class);
    }

    public ProductDto addProduct(ProductDto product) {
        Product newProduct = productRepository.save(fromDto(product, Product.class));
        return toDto(newProduct, ProductDto.class);
    }

    public ProductDto updateProduct(ProductDto product) {
        Product productTosave = fromDto(product, Product.class);
        List<String> names = product.getCategoriesNames();
        List<Category> categories = new ArrayList<>();
        for (String str : names) {
            Category category = categoryRepository.findByName(str);
            categories.add(category);
        }
        productTosave.setCategories(categories);
        categoryRepository.saveAll(categories);
        return toDto(productRepository.save(productTosave), ProductDto.class);
    }
}
