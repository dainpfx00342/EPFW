package funix.epfw.controller.user;

import funix.epfw.controller.auth.userAuth.AdminAuth;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.model.user.User;
import funix.epfw.service.productService.ProductService;
import funix.epfw.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class DeleteUser {
    private final UserService userService;

    @Autowired
    public DeleteUser(UserService userService, ProductService productService) {
        this.userService = userService;

    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes model, HttpSession session) {
        AuthChecker authChecker = new AdminAuth();
        String accessCheck = authChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        //Xoa user trong bang users
        userService.deleteUserById(id);
        model.addFlashAttribute("successMessage", "Xóa người dùng thành công!");

        //Cap nhat lai danh sach user
        List<User> users = userService.findAllUserOrderByUsername();
        model.addFlashAttribute("users", users);
             return "redirect:/manageUser"; // Chuyển hướng về danh sách user
    }
}
