package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
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
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        User user = (User) session.getAttribute("loggedInUser");
        List<Farm> farms;
        if(user.getRole()== Role.ADMIN){
            farms = farmService.findAll();
        }else {
            farms = farmService.findByUserId(user.getId());
        }

        model.addAttribute("user", user);
        model.addAttribute("farms", farms);
        return ViewPaths.MANAGE_FRAM;
    }

}
