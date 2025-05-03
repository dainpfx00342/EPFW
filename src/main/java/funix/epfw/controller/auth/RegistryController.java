package funix.epfw.controller.auth;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller

public class RegistryController {

    private final UserService userService;

    @Autowired
    public RegistryController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/registry")
    public String registry(Model model) {
        model.addAttribute("user", new User());
        return ViewPaths.REGISTER;
    }

    @PostMapping("/registry")
    public String registry(@Validated @ModelAttribute("user") User user,
                           BindingResult result, Model model,
                           @RequestParam("confirmPassword") String confirmPassword,
                           HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Đăng ký không thành công, vui lòng nhập lại thông tin!");
            model.addAttribute("user", user);
            return ViewPaths.REGISTER;
        }
        if(userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute(Message.ERROR_MESS, "Đăng ký không thành công, bạn không thể sử dụng tên đăng nhập này!");
            return ViewPaths.REGISTER;
        }
        if(!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmpassError", "Mật khẩu, và xác thực mật khẩu không khớp!");
            model.addAttribute(Message.ERROR_MESS, "Đăng ký không thành công!");
            return ViewPaths.REGISTER;
        }
        userService.saveUser(user);

        session.setAttribute("loggedInUser", user);
        session.setAttribute("role", user.getRole().name());
        return "redirect:/home";
    }


}
