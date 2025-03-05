package funix.epfw.controller.manageFarm;
/*
* Controller for add new farm
* */
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.productService.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddFarm {
    //declare farmService
    private final FarmService farmService;

    @Autowired
    public AddFarm(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/addFarm")
    public String addFarm(Model model) {
        model.addAttribute("farm", new Farm());
        return "/farm/addFarm";
    }

    @PostMapping("/addFarm")
    public String addFarm(@Validated @ModelAttribute("farm") Farm farm,
                          BindingResult result, Model model, HttpSession session ) {
        User user = (User) session.getAttribute("loggedInUser");
        farm.setUser(user);
        if (result.hasErrors()) {
            return "/farm/addFarm";
        }

        farmService.saveFarm(farm);
        return "redirect:/manageFarm";
    }
}
