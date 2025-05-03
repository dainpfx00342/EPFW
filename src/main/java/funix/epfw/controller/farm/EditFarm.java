package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Category;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class
EditFarm {
    private final FarmService farmService;

    @Autowired
    public EditFarm(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/editFarm/{farmId}")
    public String editFarm (@PathVariable Long farmId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        Farm farm = farmService.findById(farmId);
        if (farm == null) {
            return "redirect:/manageFarm?error=farmNotFound";
        }

        List<Category> categeries = Arrays.asList(Category.values());
        model.addAttribute("categories", categeries);
        model.addAttribute("farm", farm);

        return ViewPaths.EDIT_FARM;
    }
    @PostMapping("/editFarm/{farmId}")
    public String editFarm(@Validated @ModelAttribute("farm") Farm currFarm,
                           BindingResult result,
                           @PathVariable Long farmId,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        Farm farmToUpDate = farmService.findById(farmId);
        if(result.hasErrors()) {
            model.addAttribute("farm", currFarm);// gửi lại dữ liệu khi người dùng nhập sai
            model.addAttribute(Message.ERROR_MESS,"Sửa sản phẩm không thành công vui lòng thử lại!");
            return ViewPaths.EDIT_FARM;
        }

        farmToUpDate.setFarmName(currFarm.getFarmName());
        farmToUpDate.setDescription(currFarm.getDescription());
        farmToUpDate.setAddress(currFarm.getAddress());
        farmToUpDate.setCategory(currFarm.getCategory());

        farmService.saveFarm(farmToUpDate);

        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Cập nhật thông tin farm thành công");
        return "redirect:/manageFarm";
    }
}
