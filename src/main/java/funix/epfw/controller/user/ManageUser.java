package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class ManageUser {
    private final UserService userService;

    @Autowired
    public ManageUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/manageUser")
    public String editUser(Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if (checkAuth != null) {
            return checkAuth; // Chuyển hướng nếu không có quyền
        }

        List<User> users = userService.findAllUserOrderByUsername();
        model.addAttribute("users", users);
        return ViewPaths.MANAGE_USER;
    }


}
