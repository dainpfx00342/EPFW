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

    @GetMapping("/editUser/{userId}")
    public String showEditForm(@PathVariable Long userId, Model model, HttpSession session, RedirectAttributes reModel) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUuser = userService.findById(userId);
        if(currentUuser == null) {
            reModel.addFlashAttribute(Message.ERROR_MESS, "Người dùng không tồn tại!");
            return "redirect:/manageUser";

        }
        model.addAttribute("user", currentUuser);
        return ViewPaths.EDIT_USER;
    }

    @PostMapping("/editUser/{userId}")
    public String editUser(@Validated @ModelAttribute("user") User currentUser,
                           BindingResult result,
                           @PathVariable Long userId,
                           Model model, RedirectAttributes reModel) {
        // Lấy user hiện tại từ DB
        User userToUpdate = userService.findById(userId);
        if (userToUpdate == null) {
            reModel.addFlashAttribute(Message.ERROR_MESS, "Người dùng không tồn tại!");
            return "redirect:/manageUser";
        }


        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Cập nhật người dùng không thành công!");
            model.addAttribute("user", userToUpdate);
            return ViewPaths.EDIT_USER;
        }


        currentUser.setPassword(userToUpdate.getPassword());

       if(userService.findByUsername(currentUser.getUsername()) != null && !currentUser.getId().equals(userToUpdate.getId())) {
            model.addAttribute(Message.ERROR_MESS, "Username đã tồn tại, vui lòng chọn username khác.");
            model.addAttribute("user", userToUpdate);
            return ViewPaths.EDIT_USER;
        }

        userToUpdate.setUsername(currentUser.getUsername());
        userToUpdate.setAddress(currentUser.getAddress());
        userToUpdate.setPhone(currentUser.getPhone());
        userToUpdate.setRole(currentUser.getRole());
        userToUpdate.setEmail(currentUser.getEmail());
        userToUpdate.setPassword(currentUser.getPassword()); // Giữ nguyên mật khẩu cũ

        // Lưu user vào database
        userService.saveUser(userToUpdate);

        reModel.addFlashAttribute(Message.SUCCESS_MESS, "Cập nhật người dùng thành công!");
        return "redirect:/manageUser";
    }
}
