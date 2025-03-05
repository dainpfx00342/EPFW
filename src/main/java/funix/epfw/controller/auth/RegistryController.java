package funix.epfw.controller.auth;

import funix.epfw.model.user.User;
import funix.epfw.service.UserService;
import jakarta.servlet.http.HttpSession;
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
        return "/user/auth/registry";
    }

    @PostMapping("/registry")
    public String registry(@Validated @ModelAttribute("user") User user,
                           BindingResult result, Model model,
                           @RequestParam("confirmPassword") String confirmPassword,
                           HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("registrationError", "Đăng ký không thành công");
            return "/user/auth/registry";
        }
        if(userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("registrationError", "Tên đăng nhập đã tồn tại");
            return "/user/auth/registry";
        }
        if(!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmpassError", "Mật khẩu không khớp");
            return "user/auth/registry";
        }
        userService.saveUser(user);

        session.setAttribute("loggedInUser", user);
        session.setAttribute("role", user.getRole().name());
        return "/home/home";
    }


}
