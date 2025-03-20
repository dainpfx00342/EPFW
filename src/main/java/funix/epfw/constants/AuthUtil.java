package funix.epfw.constants;

import funix.epfw.controller.auth.userAuth.*;
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

    public static String checkAuth(HttpSession session) {
        AuthChecker authChecker = new UserAuth();
        return authChecker.checkAuth(session);
    }

}
