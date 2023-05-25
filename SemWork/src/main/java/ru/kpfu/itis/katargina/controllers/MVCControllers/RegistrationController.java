package ru.kpfu.itis.katargina.controllers.MVCControllers;

import jakarta.jws.soap.SOAPBinding;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.dto.SubscriptionDto;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.models.Product;
import ru.kpfu.itis.katargina.models.UserProduct;
import ru.kpfu.itis.katargina.services.ProductService;
import ru.kpfu.itis.katargina.services.SubscriptionService;
import ru.kpfu.itis.katargina.services.UserProductsService;
import ru.kpfu.itis.katargina.services.UserService;
import ru.kpfu.itis.katargina.utils.GetYearsService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    private final SubscriptionService subscriptionService;

    private final UserProductsService userProductsService;

    private final ProductService productService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(
            @Valid @ModelAttribute("user") UserDto user,
            BindingResult result) {
        if (result.hasErrors()) {
            return "registerForm";
        } else {
            user.setYears(GetYearsService.getYears(user.getBirthday()));
            UserDto userDto = userService.saveUser(user);
            SubscriptionDto subscription = subscriptionService.save(SubscriptionDto.builder()
                    .userEmail(user.getEmail())
                    .build());

            userDto.setSubscriptionId(subscription.getId());
            UserDto newUser = userService.saveUser(userDto);
            subscriptionService.save(SubscriptionDto.builder()
                            .id(subscription.getId())
                    .userEmail(newUser.getEmail())
                    .build());
            for (ProductDto product : productService.getAllProducts()){
                userProductsService.save(newUser, product, false);
            }
            return "redirect:" + MvcUriComponentsBuilder.fromMappingName("MPC#mainPage").build();
        }
    }
}
