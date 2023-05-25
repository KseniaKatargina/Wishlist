package ru.kpfu.itis.katargina.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.dto.WishlistDto;
import ru.kpfu.itis.katargina.exceptions.NotFoundException;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.User;
import ru.kpfu.itis.katargina.models.Wishlist;
import ru.kpfu.itis.katargina.repositories.ProductRepository;
import ru.kpfu.itis.katargina.repositories.WishlistRepository;

import java.util.List;

import static ru.kpfu.itis.katargina.utils.DtoConverter.*;

@RequiredArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public boolean isProductInList(Long listID, Long prodID) {
        WishlistDto wishlist = toDto(wishlistRepository.findWishlistById(listID), WishlistDto.class);
        List<Long> productIds = wishlist.getProductIds();
        return productIds.contains(prodID);
    }

    public void addProductInListEntry(Long listID, Long prodID) {
        Wishlist wishlist = wishlistRepository.findById(listID)
                .orElseThrow(() -> new NotFoundException("Wishlist not found with id " + listID));

        Product product = productRepository.findById(prodID)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + prodID));

        if (!isProductInList(listID, prodID)) {
            wishlist.addProduct(product);
            wishlistRepository.save(wishlist);
        }
    }

    public void addWishlist(Long userID, String title, String description) {
        WishlistDto wishlist = new WishlistDto(title, description, userID);
        wishlistRepository.save(fromDto(wishlist, Wishlist.class));
    }

    public List<WishlistDto> getUsersLists(UserDto user){
       return toDtoList(wishlistRepository.findAllByUserId(user.getId()), WishlistDto.class);
    }

    public void removeWishlist(Long listID) {
        wishlistRepository.deleteById(listID);
    }

    public void updateWishlist(String title, String description, Long listID, UserDto user) {
        Wishlist wishlist = wishlistRepository.findByIdAndUser(listID, fromDto(user, User.class));
        if (title != null && !title.equals("")){
        wishlist.setTitle(title);
        }
        if (description != null && !description.equals("")){
            wishlist.setDescription(description);
        }
        wishlistRepository.save(wishlist);
    }

    public WishlistDto getWishlistById(Long listID){
        return toDto(wishlistRepository.findById(listID).get(), WishlistDto.class);
    }
}
