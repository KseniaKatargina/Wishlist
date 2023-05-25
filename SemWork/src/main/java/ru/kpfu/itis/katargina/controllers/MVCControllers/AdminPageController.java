package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.katargina.dto.CategoryDto;
import ru.kpfu.itis.katargina.dto.ProductDto;
import ru.kpfu.itis.katargina.services.CategoryService;
import ru.kpfu.itis.katargina.services.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPageController {
    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @GetMapping("/adminPage/addProduct")
    public String adminPageAddProduct() {
        return "addProduct";
    }

    @GetMapping("/adminPage/editProduct/{productId}")
    public String adminPageEditProduct(@PathVariable Long productId,
                                       Model model) {
        ProductDto product = productService.getProductById(productId);
        List<String> categories = categoryService.findByProductId(productId).stream().map(CategoryDto::getName).toList();
        model.addAttribute("id", productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "editProduct";
    }


    @PostMapping("/adminPage/editProduct/{productId}")
    public String updateProduct(@PathVariable(value = "productId") Long productId,
                                @ModelAttribute ProductDto productDetails,
                                @ModelAttribute("categoriesNames") List<String> categories) {
        ProductDto product = productService.getProductById(productId);
        product.setCategoriesNames(categories);
        product.setText(productDetails.getText());
        product.setImg(productDetails.getImg());
        productService.updateProduct(product);
        return "adminPage";
    }
}
