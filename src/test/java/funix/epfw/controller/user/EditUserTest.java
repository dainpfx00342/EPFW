package funix.epfw.controller.user;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditUserTest {

    @InjectMocks
    private EditUser editUser;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;

    @Test
    void testShowEditForm_NoAuth() {
        // Giả lập người dùng không có quyền admin
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn("redirect:/login");

            String result = editUser.showEditForm(1L, model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testShowEditForm_UserNotFound() {
        String result = editUser.showEditForm(1L, model, session);
        assertEquals("redirect:/accessDenied", result);
    }

    @Test
    void testShowEditForm_WithAuth() {
        // Giả lập người dùng có quyền admin
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);

            // Tạo một người dùng giả lập
            User user = new User();
            when(userService.findById(1L)).thenReturn(user);

            String result = editUser.showEditForm(1L, model, session);
            assertEquals(ViewPaths.EDIT_USER, result);
            verify(model).addAttribute("user", user);
        }
    }

    @Test
    void testEditUser_ValidationErrors() {
        // Giả lập có lỗi validation
        User currentUser = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = editUser.editUser(currentUser, bindingResult, 1L, model, redirectAttributes);

        assertEquals(ViewPaths.EDIT_USER, result);
        verify(model).addAttribute(Message.ERROR_MESS, "Cập nhật người dùng không thành công!");
    }

    @Test
    void testEditUser_Success() {
        // Giả lập không có lỗi validation và cập nhật thành công
        User currentUser = new User();
        currentUser.setAddress("New Address");
        currentUser.setPhone("123456789");
        currentUser.setRole(Role.BUYER);
        currentUser.setEmail("newemail@example.com");

        User userToUpdate = new User();
        userToUpdate.setPassword("oldPassword");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findById(1L)).thenReturn(userToUpdate);

        String result = editUser.editUser(currentUser, bindingResult, 1L, model, redirectAttributes);

        assertEquals("redirect:/manageUser", result);
        verify(userService).saveUser(userToUpdate);
        verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Cập nhật người dùng thành công!");
    }
}