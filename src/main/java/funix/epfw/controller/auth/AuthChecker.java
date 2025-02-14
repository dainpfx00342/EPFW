package funix.epfw.controller.auth;

import jakarta.servlet.http.HttpSession;

public interface AuthChecker {
    String checkAuth (HttpSession session);
}
