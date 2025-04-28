package funix.epfw.controller.auth.userAuth;

import funix.epfw.constants.Role;
import funix.epfw.model.user.User;
import jakarta.servlet.http.HttpSession;

public class FarmerAuth implements AuthChecker {
    @Override
    public String checkAuth(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/accessDenied";
        }
        Role role = user.getRole();
        if(role != Role.FARMER) {
            return "redirect:/accessDenied";
        }
        return null;
    }
}
