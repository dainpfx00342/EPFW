package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailFarm {
    private final FarmService farmService;

    @Autowired
    public DetailFarm(FarmService farmService) {
        this.farmService = farmService;
    }
    @GetMapping("/farmDetail/{farmId}")
    public String farmDetail(@PathVariable Long farmId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        Farm farm = farmService.findById(farmId);
        model.addAttribute("farm", farm);
        return ViewPaths.DETAIL_FARM;
    }
}
