package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.katargina.dto.UserDto;
import ru.kpfu.itis.katargina.models.forms.UserForm;
import ru.kpfu.itis.katargina.services.UserService;
import ru.kpfu.itis.katargina.utils.GetYearsService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/editProfile", method = RequestMethod.GET)
    public String showEditProfileForm(Model model, Principal principal){
       String email = principal.getName();
       UserDto user = userService.findByEmail(email);

        UserForm userForm = new UserForm();
        userForm.setUsername(user.getUsername());
        userForm.setEmail(user.getEmail());
        userForm.setBirthday(user.getBirthday());
        userForm.setOldPassword(user.getPassword());

        model.addAttribute("userForm", userForm);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model, Principal principal) {

        String email = principal.getName();
        UserDto user = userService.findByEmail(email);

        if (userForm.getUsername() != null && userForm.getUsername().length() > 20) {
            bindingResult.rejectValue("username", "error.userForm", "Username не должен быть пустым и не должен содержать более 20 символов");
            return "editProfile";
        }

        if (userForm.getEmail() != null && !userForm.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            bindingResult.rejectValue("email", "error.userForm", "Неккоректный email");
            return "editProfile";
        }

        if (userForm.getUsername() != null && !userForm.getUsername().isEmpty()) {
            user.setUsername(userForm.getUsername());
        }

        if (userForm.getEmail() != null && !userForm.getEmail().isEmpty()) {
            user.setEmail(userForm.getEmail());
        }

        if (userForm.getBirthday() != null) {
            user.setBirthday(userForm.getBirthday());
        }

        if (userForm.getOldPassword() != null && !userForm.getOldPassword().isEmpty()
                && userForm.getNewPassword() != null && !userForm.getNewPassword().isEmpty()
                && userForm.getRePassword() != null && !userForm.getRePassword().isEmpty()) {
            if (passwordEncoder.matches(userForm.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userForm.getNewPassword()));
            } else {
                bindingResult.rejectValue("oldPassword", "error.userForm", "Неверный старый пароль");
                return "editProfile";
            }
        }
        userService.saveUser(user);
        return "redirect:/profile";
    }


    @GetMapping("/profile")
    public String getProfilePage(Model model, Principal principal){
        String email = principal.getName();
        UserDto user = userService.findByEmail(email);
        user.setYears(GetYearsService.getYears(user.getBirthday()));
        model.addAttribute("userDto", user);
        return "profile";
    }
}

