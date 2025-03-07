package funix.epfw.controller.farm;

import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.productService.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/*
* Controller for manage farm
* */
@Controller
public class ManageFarm {
    private final FarmService farmService;

    @Autowired
    public ManageFarm(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/manageFarm")
    public String manageFarm(Model model, HttpSession session) {
        AuthChecker authChecker = new FarmerAuth();
        String accessCheck = authChecker.checkAuth(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        User user = (User) session.getAttribute("loggedInUser");
        //get all farm by user id
        List<Farm> farm = farmService.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("farms", farm);
        return "farm/manageFarm";
    }

}
