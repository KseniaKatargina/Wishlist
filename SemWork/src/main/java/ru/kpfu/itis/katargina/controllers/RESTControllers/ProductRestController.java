package ru.kpfu.itis.katargina.controllers.RESTControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.services.*;
import ru.kpfu.itis.katargina.utils.ImageRecognitionUtil;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final WishlistService wishlistService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserProductsService userProductsService;
    private final UserService userService;


    @PostMapping("/filterProducts/{prodID}/{listID}")
    public ResponseEntity<?> addProductToWishlist(@PathVariable Long prodID,
                                                  @PathVariable Long listID) {
        if (wishlistService.isProductInList(listID, prodID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            wishlistService.addProductInListEntry(listID, prodID);
            return ResponseEntity.ok().build();
        }
    }


    @PostMapping("/products/filter")
    public List<ProductDto> filterProducts(@RequestBody List<Integer> selectedCategories) {
        return productService.getProductsByCategories(selectedCategories);
    }


    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(value = "id") Long productId) {
        ProductDto product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        List<CategoryDto> productCategories = categoryService.findByProductId(productId);
        product.setCategoriesNames(productCategories.stream().map(CategoryDto::getName).toList());
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/products")
    public ProductDto addProduct(@RequestBody ProductDto product) {
        ProductDto productDto = productService.addProduct(product);
        for (UserDto user : userService.findAll()){
            userProductsService.save(user, productDto, false);
        }
        List<String> categories = ImageRecognitionUtil.recognizeImage(product.getImg());
        List<CategoryDto> categoriesToSave = categories.stream()
                .map(category -> CategoryDto.builder().
                        name(category)
                        .productsIds(Collections.singletonList(productDto.getId()))
                        .build())
                .toList();
        categoryService.addAllCategoriesByProductId(categoriesToSave, productDto.getId());

        return productDto;
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") Long productId) {
        ProductDto product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        for (UserDto user : userService.findAll()){
            userProductsService.delete(user, product);
        }
        productService.deleteProduct(product);
        return ResponseEntity.ok().build();
    }

}
