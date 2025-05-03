package funix.epfw.controller.user;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;

import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EditProfileTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EditProfile editProfile;

    private User loggedInUser;

    @BeforeEach
    void setUp() {

        loggedInUser = new User();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("testuser");
        loggedInUser.setPhone("1234567890");
        loggedInUser.setEmail("test@example.com");
        loggedInUser.setAddress("Test Address");
    }

    @Test
    void testEditProfile_Success() {
        when(session.getAttribute("loggedInUser")).thenReturn(loggedInUser);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.findByUsername(anyString())).thenReturn(null);

        User updatedUser = new User();
        updatedUser.setUsername("newuser");
        updatedUser.setPhone("0987654321");
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setAddress("New Address");

        String result = editProfile.editProfile(updatedUser, this.bindingResult, model, session);

        assertEquals(ViewPaths.EDIT_PROFILE, result);
        verify(userService, times(1)).saveUser(loggedInUser);
        verify(session, times(1)).setAttribute("loggedInUser", loggedInUser);
    }

    @Test
    void testEditProfile_UserNotFound() {
        when(session.getAttribute("loggedInUser")).thenReturn(null);

        String result = editProfile.editProfile(new User(), bindingResult, model, session);

        assertEquals(ViewPaths.EDIT_PROFILE, result);
        verify(model, times(1)).addAttribute(Message.ERROR_MESS, "Không tìm thấy người dùng");
    }

    @Test
    void testEditProfile_UsernameAlreadyExists() {
        when(session.getAttribute("loggedInUser")).thenReturn(loggedInUser);
        when(bindingResult.hasErrors()).thenReturn(false);

        User existingUser = new User();
        existingUser.setId(2L);  // Different ID to simulate another user
        existingUser.setUsername("existinguser");
        when(userService.findByUsername("existinguser")).thenReturn(existingUser);

        User updatedUser = new User();
        updatedUser.setUsername("existinguser"); // Same username as existing one

        String result = editProfile.editProfile(updatedUser, bindingResult, model, session);

        assertEquals(ViewPaths.EDIT_PROFILE, result);
        verify(model, times(1)).addAttribute(Message.ERROR_MESS, "Username đã tồn tại, vui lòng chọn username khác.");
    }

    @Test
    void testEditProfile_ValidationError() {

        when(bindingResult.hasErrors()).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setUsername("");  // Invalid username to trigger validation error

        String result = editProfile.editProfile(updatedUser, bindingResult, model, session);

        assertEquals(ViewPaths.EDIT_PROFILE, result);
        verify(model, times(1)).addAttribute(Message.ERROR_MESS, "Thay đổi thông tin không thành công");
    }
}