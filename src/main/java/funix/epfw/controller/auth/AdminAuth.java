package funix.epfw.controller.auth;

import funix.epfw.constants.ROLE;
import funix.epfw.model.User;
import jakarta.servlet.http.HttpSession;

public class AdminAuth implements AuthChecker {

    @Override
    public String checkAuth(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/login";
        }
        ROLE role = user.getRole();
        if(role != ROLE.ADMIN) {
            return "redirect:/auth/accessDenied";
        }
        return null;
    }
}
