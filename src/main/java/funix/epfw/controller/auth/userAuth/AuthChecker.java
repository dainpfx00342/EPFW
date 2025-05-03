package funix.epfw.controller.auth.userAuth;

import jakarta.servlet.http.HttpSession;

public interface AuthChecker {
   String checkAuth (HttpSession session);
}
