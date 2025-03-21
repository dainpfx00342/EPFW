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
    public String showEditForm(@PathVariable Long userId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUuser = userService.findById(userId);
        if(currentUuser == null) {
            // Handle error
            return "redirect:/login";
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

        // Kiểm tra lỗi validation trước khi cập nhật mật khẩu
        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Cập nhật người dùng không thành công!");
            model.addAttribute("user", userToUpdate);
            return ViewPaths.EDIT_USER;
        }

        // Giữ lại mật khẩu cũ
        currentUser.setPassword(userToUpdate.getPassword());

        // Cập nhật thông tin user nhưng giữ nguyên mật khẩu cũ
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
