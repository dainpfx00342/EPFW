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

            String result = editUser.showEditForm(1L, model, session, redirectAttributes);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testShowEditForm_UserNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            // 1. Cho phép qua được checkAdminAuth
            authUtil.when(() -> AuthUtil.checkAdminAuth(session))
                    .thenReturn(null);

            // 2. Khi tìm từ service trả về null (user không tồn tại)
            when(userService.findById(1L)).thenReturn(null);

            // 3. Gọi controller bên trong block mockStatic
            String result = editUser.showEditForm(1L, model, session, redirectAttributes);

            // Xác nhận đúng redirect khi user không tồn tại
            assertEquals("redirect:/manageUser", result);
            verify(redirectAttributes)
                    .addFlashAttribute(Message.ERROR_MESS, "Người dùng không tồn tại!");
        }
    }

    @Test
    void testShowEditForm_WithAuth() {
        // Giả lập người dùng có quyền admin
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);

            // Tạo một người dùng giả lập
            User user = new User();
            when(userService.findById(1L)).thenReturn(user);

            String result = editUser.showEditForm(1L, model, session, redirectAttributes);
            assertEquals(ViewPaths.EDIT_USER, result);
            verify(model).addAttribute("user", user);
        }
    }

    @Test
    void testEditUser_ValidationErrors() {
        // 1. Giả lập có user trong DB
        User userToUpdate = new User();
        when(userService.findById(1L)).thenReturn(userToUpdate);

        // 2. Giả lập có lỗi validation
        when(bindingResult.hasErrors()).thenReturn(true);

        User currentUser = new User();
        String result = editUser.editUser(
                currentUser,
                bindingResult,
                1L,
                model,
                redirectAttributes
        );

        // 3. Kỳ vọng vào branch lỗi validation
        assertEquals(ViewPaths.EDIT_USER, result);
        verify(model).addAttribute(Message.ERROR_MESS, "Cập nhật người dùng không thành công!");
        // Và controller sẽ thêm lại user gốc vào model
        verify(model).addAttribute("user", userToUpdate);
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