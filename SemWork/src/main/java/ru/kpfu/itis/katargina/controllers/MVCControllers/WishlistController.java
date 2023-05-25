package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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
public class WishlistController {

    private final WishlistService wishlistService;

    private final UserService userService;

    private final ProductService productService;

    @GetMapping("/myWishlists")
    public String showUserWishlists(Principal principal, ModelMap map){
        String email = principal.getName();
        UserDto user = userService.findByEmail(email);
        List<WishlistDto> wishlists =  wishlistService.getUsersLists(user);
        map.put("wishlists", wishlists);
        return "userWishlists";
    }

    @RequestMapping(value = "/addWishlist", method = RequestMethod.GET)
    public String showAddWishlistForm(Model model){
        model.addAttribute("user", new WishlistDto());
        return "addWishlist";
    }

    @RequestMapping(value = "/addWishlist", method = RequestMethod.POST)
    public String addWishlist(@ModelAttribute("wishlist") WishlistDto wishlist,
                              Principal principal){
        String email = principal.getName();
        UserDto user = userService.findByEmail(email);
        wishlistService.addWishlist(user.getId(), wishlist.getTitle(), wishlist.getDescription());
        return "redirect:/myWishlists";
    }

    @PostMapping("/removeWishlist")
    public String removeWishlist(@RequestParam(value = "listID") Long listID) {
        wishlistService.removeWishlist(listID);
        return "redirect:/myWishlists";
    }

    @RequestMapping(value = "/changeWishlist", method = RequestMethod.GET)
    public String showEditWishlistPage(@RequestParam Long listID,
                                       Model model){
        model.addAttribute("listID", listID);
        return "changeWishlist";
    }

    @RequestMapping(value = "/changeWishlist", method = RequestMethod.POST)
    public String changeWishlist(@ModelAttribute(value = "title") String title,
                                 @ModelAttribute(value = "listID") String listID,
                                 @ModelAttribute(value = "description") String description,
                                 Principal principal)
    {
        String email = principal.getName();
        UserDto user = userService.findByEmail(email);
        wishlistService.updateWishlist(title, description, Long.valueOf(listID), user);
        return "redirect:/myWishlists";
    }

    @RequestMapping(value = "/wishlist", method = RequestMethod.GET)
    public String showWishlist(@RequestParam("listID") Long listID,
                               Model model){
        WishlistDto wishlist = wishlistService.getWishlistById(listID);

        List<ProductDto> products = productService.getAllByWishListId(listID);
        model.addAttribute("products", products);
        model.addAttribute("title", wishlist.getTitle());
        model.addAttribute("description", wishlist.getDescription());
        model.addAttribute("listID", wishlist.getId());
        return "wishlist";
    }
}
