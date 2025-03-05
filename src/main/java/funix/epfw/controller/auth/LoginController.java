package funix.epfw.controller.auth;

import funix.epfw.model.user.User;
import funix.epfw.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Để hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        //Xoá session cũ
       session.invalidate();
        return "/auth/login"; // Trả về trang đăng nhập
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session, Model model) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {

            session.setAttribute("loggedInUser", user);
            session.setAttribute("role", user.getRole().name());
            return "redirect:/home";
        }
        model.addAttribute("errorMess", "Sai tên đăng nhập hoặc mật khẩu");
        return "/auth/login";
    }
}

