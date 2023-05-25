package ru.kpfu.itis.katargina.controllers.MVCControllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.dto.SubscriptionDto;
import ru.kpfu.itis.katargina.services.CategoryService;
import ru.kpfu.itis.katargina.services.ProductService;
import ru.kpfu.itis.katargina.services.SubscriptionService;
import ru.kpfu.itis.katargina.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    private final CategoryService categoryService;

    private final ProductService productService;

    private final UserService userService;


    @GetMapping("/subscribe")
    public String getSubcribePage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "subscription";
    }

    @GetMapping("/subscriptions")
    public String getSubscriptionsPage(Model model,
                                       Principal principal) {
        SubscriptionDto userSub = subscriptionService.getUsersSubs(principal.getName());
        if (userSub != null) {
            List<CategoryDto> subsCategories = categoryService.getAllSubscribedCategories(userSub);
            model.addAttribute("subscribedCategories", subsCategories);
            List<ProductDto> products = productService.getUnSeenProducts(userService.findByEmail(principal.getName()).getId());
            List<ProductDto> unSeenProducts = new ArrayList<>();
            for (CategoryDto category : subsCategories) {
                for (ProductDto product : products) {
                    if (product.getCategoriesNames().contains(category.getName())){
                        unSeenProducts.add(product);
                    }
                }
            }
            model.addAttribute("unseenProducts", unSeenProducts);
        }

        return "subscriptions";
    }

    @PostMapping("/subscribe")
    public String handleSubscriptionForm(@RequestParam("categoryIds") List<Long> categoryIds,
                                         Principal principal) {
        SubscriptionDto subscriptionDto = subscriptionService.getUsersSubs(principal.getName());
        subscriptionDto.setCategoriesIds(categoryIds);
        subscriptionDto.setUserEmail(principal.getName());
        subscriptionService.save(subscriptionDto);

        return "redirect:/profile";
    }


}
