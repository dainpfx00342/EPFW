package funix.epfw.controller.auth.userAuth;

import funix.epfw.model.user.User;
import jakarta.servlet.http.HttpSession;

public class UserAuth implements AuthChecker {
    @Override
    public String checkAuth(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            return "redirect:/accessDenied";
        }
        return null;
    }
}
