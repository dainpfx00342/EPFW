package funix.epfw.constants;

import funix.epfw.controller.auth.userAuth.AdminAuth;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.BuyerAuth;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import jakarta.servlet.http.HttpSession;

public class AuthUtil {

    //check ADMIN role
    public static  String checkAdminAuth(HttpSession session) {
        AuthChecker  authChecker = new AdminAuth();
        return authChecker.checkAuth(session);
    }

    //check FARMER role
    public static String checkFarmerAuth(HttpSession session) {
        AuthChecker authChecker = new FarmerAuth();
        return authChecker.checkAuth(session);
    }

    //check Buyer role
    public static String checkBuyerAuth(HttpSession session) {
        AuthChecker  authChecker = new BuyerAuth();
         return authChecker.checkAuth(session);
    }
}
