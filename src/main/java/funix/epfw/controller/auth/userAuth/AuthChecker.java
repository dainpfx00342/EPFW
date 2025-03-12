package funix.epfw.controller.auth.userAuth;

import funix.epfw.constants.Message;
import jakarta.servlet.http.HttpSession;

public interface AuthChecker {
   String checkAuth (HttpSession session);
}
