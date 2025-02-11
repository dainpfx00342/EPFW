package funix.epfw.controller;

import funix.epfw.model.User;
import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
public class RegistryController {

    private final UserService userService;
    @Autowired
    public RegistryController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/registry")
    public String registry(Model model) {
        model.addAttribute("user", new User());
        return "registry";
    }

    @PostMapping("/registry")
    public String registry(@Validated @ModelAttribute("user") User user,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registrationError", "Đăng ký không thành công");
            return "registry";
        }
        if(userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("registrationError", "Tên đăng nhập đã tồn tại");
            return "registry";
        }
        userService.saveUser(user);
        return "redirect:/home";
    }


}
