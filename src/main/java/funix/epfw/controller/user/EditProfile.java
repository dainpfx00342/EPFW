package funix.epfw.controller.user;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditProfile {
    private final UserService userService;

    @Autowired
    public EditProfile(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);

        return ViewPaths.EDIT_PROFILE;
    }

    @PostMapping("/editProfile")
    public String editProfile( @Validated @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model,
                               HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Thay đổi thông tin không thành công");
            return ViewPaths.EDIT_PROFILE;
        }

        // Lấy user từ session
        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) {
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy người dùng");
            return ViewPaths.EDIT_PROFILE;
        }
        // Cập nhật thông tin từ form vào session user
        sessionUser.setUsername(user.getUsername());
        sessionUser.setPhone(user.getPhone());
        sessionUser.setEmail(user.getEmail());
        sessionUser.setAddress(user.getAddress());

        session.setAttribute("loggedInUser", sessionUser);
        userService.saveUser(sessionUser);
        model.addAttribute(Message.SUCCESS_MESS, "Cập nhật người dùng thành công");


        return ViewPaths.EDIT_PROFILE;

    }
}
