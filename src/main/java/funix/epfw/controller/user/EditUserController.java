package funix.epfw.controller.user;

import funix.epfw.model.User;
import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EditUserController {
    private final UserService userService;

    @Autowired
    public EditUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editUser")
    public String editUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "editUser";
    }
}
