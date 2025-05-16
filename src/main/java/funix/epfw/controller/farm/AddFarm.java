package funix.epfw.controller.farm;
/*
* Controller for add new farm
* */
import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Category;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class AddFarm {

    private final FarmService farmService;

    @Autowired
    public AddFarm(FarmService farmService) {

        this.farmService = farmService;
    }

    @GetMapping("/addFarm")
    public String saveFarm(Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        List<Category> categeries = Arrays.asList(Category.values());
        model.addAttribute("categories", categeries);
        model.addAttribute("farm", new Farm());
        return ViewPaths.ADD_FARM;
    }

    @PostMapping("/addFarm")
    public String addFarm(@Validated @ModelAttribute("farm") Farm farm,
                          BindingResult result, Model model, HttpSession session ) {
        User user = (User) session.getAttribute("loggedInUser");

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS,"Tạo trang trại không thành công");
            return ViewPaths.ADD_FARM;
        }
        farm.setUser(user);
        farmService.saveFarm(farm);
        return "redirect:/manageFarm";
    }
}
