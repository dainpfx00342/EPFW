package funix.epfw.controller.manageUser;

import funix.epfw.model.User;
import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManageUser {
    private final UserService userService;

    @Autowired
    public ManageUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/manageUser")
    public String editUser(Model model) {
        List<User> users = userService.findAllUserOrderByUsername();
        model.addAttribute("users", users);
        return "manageUser";
    }


}
