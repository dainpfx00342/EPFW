package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.product.ProductService;
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


    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }

        productService.deleteProductById(id);
        model.addFlashAttribute(Message.SUCCESS_MESS, "Xóa sản phẩm thành công!");

        return "redirect:/manageProduct";
    }

}
