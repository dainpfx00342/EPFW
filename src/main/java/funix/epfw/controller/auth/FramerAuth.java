package funix.epfw.controller.auth;

import funix.epfw.constants.Role;
import funix.epfw.model.User;
import jakarta.servlet.http.HttpSession;

public class FramerAuth implements AuthChecker {
    @Override
    public String checkAuth(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/accessDenied";
        }
        Role role = user.getRole();
        if(role != Role.ADMIN && role != Role.FARMER) {
            return "redirect:/auth/accessDenied";
        }
        return null;
    }
}
