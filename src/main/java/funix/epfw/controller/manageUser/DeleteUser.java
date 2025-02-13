package funix.epfw.controller.manageUser;

import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteUser {
    private final UserService userService;

    @Autowired
    public DeleteUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes model) {
        userService.deleteUserById(id);
        model.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
        return "redirect:/manage_user/editUser"; // Chuyển hướng về danh sách user
    }
}
