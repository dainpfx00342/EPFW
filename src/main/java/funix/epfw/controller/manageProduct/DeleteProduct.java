package funix.epfw.controller.manageProduct;

import funix.epfw.constants.Role;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FramerAuth;
import funix.epfw.model.User;
import funix.epfw.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteProduct {

    private final ProductService productService;

    @Autowired
    public DeleteProduct(ProductService productService) {
        this.productService = productService;
    }

    // Show page Delete product
    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes model, HttpSession session) {
        AuthChecker authChecker = new FramerAuth();
        String accessCheck = authChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }

        //Xoa san pham trong bang products
        productService.deleteProductById(id);
        model.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");


        //Cap nhat lai danh sach san pham
        User currentUser = (User) session.getAttribute("loggedInUser");
        if(currentUser.getRole()!= Role.ADMIN) {
            model.addFlashAttribute("products", productService.getAllProductsByUser(currentUser));
            return "redirect:/manageProduct"; // Chuyển hướng về danh sách sản phẩm
        }
        model.addFlashAttribute("products", productService.findAll());
        return "/manage_product/manageProduct";
    }

}
