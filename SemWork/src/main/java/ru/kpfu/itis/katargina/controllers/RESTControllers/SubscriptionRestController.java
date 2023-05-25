package ru.kpfu.itis.katargina.controllers.RESTControllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.katargina.services.*;

import java.security.Principal;

@RestController
public class SubscriptionRestController {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProductsService userProductService;

    @PostMapping("/unsubscribe/{categoryId}")
    public ResponseEntity<String> unsubscribeCategory(@PathVariable Long categoryId,
                                                      Principal principal) {
        subscriptionService.unsubscribeCategory(principal.getName(), categoryId);

        return ResponseEntity.ok("Unsubscribed from category successfully");
    }

    @PostMapping("/markAsSeen/{productId}")
    public ResponseEntity<String> markProductAsSeen(@PathVariable Long productId,
                                                    Principal principal) {
        userProductService.markAsSeen(userService.findByEmail(principal.getName()).getId(), productId);

        return ResponseEntity.ok("Product marked as seen successfully");
    }
}

