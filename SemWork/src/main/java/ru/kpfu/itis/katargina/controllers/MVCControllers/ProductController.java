package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.katargina.services.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @RequestMapping(value = "/removeProduct", method = RequestMethod.POST)
    public String removeProductFromWishlist(@RequestParam(value = "prodID") Long prodID,
                                            @RequestParam(value = "listID") Long listID){
        productService.removeProductFromWishlist(prodID,listID);
        return("redirect:/myWishlists");
    }
}
