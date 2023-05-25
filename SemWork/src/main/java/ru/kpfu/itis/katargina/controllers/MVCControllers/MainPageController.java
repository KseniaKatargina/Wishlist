package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.dto.WishlistDto;
import ru.kpfu.itis.katargina.services.ProductService;
import ru.kpfu.itis.katargina.services.UserService;
import ru.kpfu.itis.katargina.services.WishlistService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final  UserService userService;

    @GetMapping({"/", "/mainPage"})
    public String mainPage(Model model, Principal principal) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        if (principal != null) {
            String email = principal.getName();
            UserDto user = userService.findByEmail(email);
            List<WishlistDto> wishlists = wishlistService.getUsersLists(user);
            model.addAttribute("wishlists", wishlists);
        }
        return "mainPage";
    }
}
