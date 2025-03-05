package funix.epfw.controller.manageProduct;


import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FramerAuth;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.productService.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class EditProduct {
    private final ProductService productService;

    @Autowired
    public EditProduct(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/editProduct")
    public String showEditProductForm(HttpSession session) {
        // Kiểm tra quyền truy cập
        AuthChecker authChecker = new FramerAuth();
        String accessCheck = authChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        return "redirect:/manageProduct";
    }

    @GetMapping("/editProduct/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model, HttpSession session) {
        // Kiểm tra quyền truy cập
        AuthChecker authChecker = new FramerAuth();
        String accessCheck = authChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        Product product = productService.findById(id);
        if(product == null) {
            // Handle error
            return "redirect:/login";
        }
        model.addAttribute("product", product);
        return "/manage_product/editProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id,
                              @Validated @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam("imageFile") MultipartFile file,
                              Model model, RedirectAttributes redirectAttributes) {
        // Tìm sản phẩm trong database
        Product productToUpdate = productService.findById(id);
        if (productToUpdate == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm cần cập nhật!");
            return "redirect:/manageProduct";
        }
        // Kiểm tra file ảnh
        try{
            String imageUrl = productService.saveImage(file);
            if(imageUrl != null){
                productToUpdate.setImageUrl(imageUrl);
            }
        }catch (IOException e){
            model.addAttribute("registrationError", "Lỗi lưu ảnh");
        }


        //kiểm tra lỗi nhập liệu
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Cập nhật sản phẩm không thành công! Dữ liệu nhập vào không hơợp lệ ");
            model.addAttribute("product", product); // Giữ lại thông tin sản phẩm để hiển thị lại form
            return "manage_product/editProduct";
        }


        //cập nhật sản phẩm
        productToUpdate.setProductName(product.getProductName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setNumberOfStock(product.getNumberOfStock());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setUpdatedTimes(LocalDateTime.now());


        //lưu sản phẩm vào database
        productService.saveProduct(productToUpdate);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
        //cap nhat lai danh sach san pham
        return "redirect:/manageProduct";
    }
}
