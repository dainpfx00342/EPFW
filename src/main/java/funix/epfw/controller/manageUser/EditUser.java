package funix.epfw.controller.manageUser;

import funix.epfw.model.User;
import funix.epfw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EditUser {

    private final UserService userService;

    @Autowired
    public EditUser(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/editUser/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Edit user
        User user = userService.findById(id);
        if(user == null) {
            // Handle error
            return "redirect:/manageUser";
        }
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, User user, RedirectAttributes model, BindingResult result) {
        // Update user
        User userToUpdate = userService.findById(id);
        if(userToUpdate == null) {
            // Handle error
            model.addFlashAttribute("registrationError", "Cập nhật người dùng không thành công!");
            return "redirect:/editUser";
        }
        if(result.hasErrors()) {
            model.addFlashAttribute("registrationError", "Cập nhật người dùng không thành công!");
            return "redirect:/editUser";
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setRole(user.getRole());
        model.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
        userService.saveUser(userToUpdate);

        return "redirect:/manageUser";
    }
}
