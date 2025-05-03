package funix.epfw.controller.farm.product;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.ProductService;
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
import java.util.Arrays;
import java.util.List;


@Controller
@SessionAttributes("loggedInUser")
public class AddProduct {

    private final ProductService productService;
    private final FarmService farmService;

    @Autowired
    public AddProduct(ProductService productService, FarmService farmService) {
        this.productService = productService;
        this.farmService = farmService;
    }

    @GetMapping("/addProduct/{farmId}")
    public String addProduct(Model model, HttpSession session, @PathVariable Long farmId, RedirectAttributes redirectAttributes) {
        String checkAuth =  AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }


        Farm farm = farmService.findById(farmId);
        if (farm==null) {
            redirectAttributes.addFlashAttribute(Message.ERROR_MESS, "Không thể tìm thấy trang trại.");
            return "redirect:/manageFarm?error=farmNotFound";
        }

        List<Unit> units = Arrays.asList(Unit.values());
        List<Category> categories = Arrays.asList(Category.values());

        model.addAttribute("categories", categories);
        model.addAttribute("units", units);
        model.addAttribute("product", new Product());
        model.addAttribute("farm", farm);

        return ViewPaths.ADD_PRODUCT;
    }


    @PostMapping("/addProduct/{farmId}")
    public String addProduct(@Validated @ModelAttribute("product") Product newProduct,
                             BindingResult result, @PathVariable Long farmId,
                             @RequestParam("imageFile") MultipartFile file,
                             Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        // Kiểm tra nếu farm không tồn tại
       Farm farm = farmService.findById(farmId);
        if (farm==null) {
            redirectAttributes.addFlashAttribute(Message.ERROR_MESS, "Không thể tìm thấy trang trại.");
            return "redirect:/manageFarm?error=farmNotFound";
        }
        try{
            String imageUrl = productService.saveImage(file);
            if(imageUrl != null){
                newProduct.setImageUrl(imageUrl);
            }
        }catch (IOException e){
            model.addAttribute(Message.ERROR_MESS, "Lỗi lưu ảnh");
        }


        if (result.hasErrors()) {
            model.addAttribute("categories", Arrays.asList(Category.values()));
            model.addAttribute("units", Arrays.asList(Unit.values()));
            model.addAttribute("farm", farm);
            model.addAttribute(Message.ERROR_MESS, "Vui lòng nhập đầy đủ và chính xác thông tin.");

            return ViewPaths.ADD_PRODUCT;
        }
        newProduct.setFarm(farm);
        productService.saveOrUpdateProduct(newProduct);
        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS,"Sản phẩm được thêm thành công");

        return "redirect:/manageProduct";

    }
}
