package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@SessionAttributes("loggedInUser")
public class DeleteUser {
    private final UserService userService;

    @Autowired
    public DeleteUser(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes model, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        userService.deleteUserById(id);
        model.addFlashAttribute(Message.SUCCESS_MESS, "Xóa người dùng thành công!");

             return "redirect:/manageUser"; // Chuyển hướng về danh sách user
    }
}
