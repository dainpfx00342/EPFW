package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpSession session;

    @InjectMocks
    private DeleteUser deleteUser;


    @Test
    void testDeleteUser_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
             authUtilMockedStatic.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn("redirect:/accessDenied");
            String view = deleteUser.deleteUser(1L, redirectAttributes, session);
            assertEquals("redirect:/accessDenied", view);
            verifyNoInteractions(userService);
            verifyNoInteractions(redirectAttributes);
        }
    }

    @Test
    void testDeleteUser_Success() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);
            String view = deleteUser.deleteUser(1L, redirectAttributes, session);

            assertEquals("redirect:/manageUser", view);
            verify(userService).deleteUserById(1L);  // Kiểm tra phương thức deleteUserById đã được gọi
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xóa người dùng thành công!");  // Kiểm tra thông báo thành công
        }
    }
}