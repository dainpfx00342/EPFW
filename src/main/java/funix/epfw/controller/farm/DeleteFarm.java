package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.model.farm.Farm;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteFarm {
    private final FarmService farmService;

    @Autowired
    public DeleteFarm(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/deleteFarm/{farmId}")
    public String deleteFarm(@PathVariable Long farmId, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        Farm farm = farmService.findById(farmId);
        if (farm == null) {
            redirectAttributes.addFlashAttribute(Message.ERROR_MESS, "Trang trại không tồn tại!");
            return "redirect:/manageFarm";
        }
        farmService.deleteFarmById(farmId);
        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS,"Xóa trang trại thành công!");
        return "redirect:/manageFarm";
    }
}
