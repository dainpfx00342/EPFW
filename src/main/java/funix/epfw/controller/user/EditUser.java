package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("loggedInUser")
public class EditUser {

    private final UserService userService;

    @Autowired
    public EditUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editUser/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User user = userService.findById(id);
        if(user == null) {
            // Handle error
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return ViewPaths.EDIT_USER;
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, @Validated @ModelAttribute("user") User user,
                           BindingResult result, Model model, RedirectAttributes reModel) {
        // Update user
        User userToUpdate = userService.findById(id);
        // Update user
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setRole(user.getRole());
        userToUpdate.setEmail(user.getEmail());
        // Validate user
        if(result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Cập nhật người dùng không thành công!");
            model.addAttribute("user", user); // Giữ lại thông tin user để hiển thị lại form
            return ViewPaths.EDIT_USER;
        }


        // Save user to database
        userService.saveUser(userToUpdate);
        reModel.addFlashAttribute(Message.SUCCESS_MESS, "Cập nhật người dùng thành công!");
        return "redirect:/manageUser";
    }
}
