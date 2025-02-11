package funix.epfw.controller.user;

import funix.epfw.model.User;
import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<User> users = userService.findAllUserOrderByUsername();
        model.addAttribute("users", users);
        return "editUser";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes model) {
        userService.deleteUserById(id);
        model.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
        return "redirect:/editUser"; // Chuyển hướng về danh sách user
    }
}
