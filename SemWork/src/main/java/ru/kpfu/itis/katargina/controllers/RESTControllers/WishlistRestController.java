package ru.kpfu.itis.katargina.controllers.RESTControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.katargina.services.ProductService;
import ru.kpfu.itis.katargina.services.WishlistService;

@RestController
@RequiredArgsConstructor
public class WishlistRestController {
    private final WishlistService wishlistService;
    private final ProductService productService;


    @PostMapping("/addProduct/{prodID}/{listID}")
    public ResponseEntity<?> addProductToWishlist(@PathVariable Long prodID,
                                                  @PathVariable Long listID) {
        if (wishlistService.isProductInList(listID, prodID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            wishlistService.addProductInListEntry(listID, prodID);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/removeProduct/{prodID}/{listID}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long prodID,
                                           @PathVariable Long listID) {
        productService.removeProductFromWishlist(prodID, listID);
        return ResponseEntity.ok().build();
    }

}
