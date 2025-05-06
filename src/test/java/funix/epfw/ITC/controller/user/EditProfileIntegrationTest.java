package funix.epfw.ITC.controller.user;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EditProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    private User sessionUser;


    @BeforeEach
    void setUp() {
        sessionUser = new User();
        sessionUser.setId(1L);
        sessionUser.setUsername("alice");
        sessionUser.setPhone("0123456789");
        sessionUser.setEmail("alice@example.com");
        sessionUser.setAddress("Hanoi");
    }
    // TC1: GET /editProfile, không có session → chuyển về ACCESS_DENIED
    @Test
    void editProfile_get_noSession_shouldAccessDenied() throws Exception {
        mockMvc.perform(get("/editProfile"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ACCESS_DENIED));
    }

    // TC2: GET /editProfile, có session → trả về form Edit Profile
    @Test
    void editProfile_get_withSession_shouldShowForm() throws Exception {
        mockMvc.perform(get("/editProfile")
                        .sessionAttr("loggedInUser", sessionUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PROFILE))
                .andExpect(model().attribute("user", sessionUser));
    }

    // TC3: POST /editProfile, có lỗi validation (username để trống) → trả về form với ERROR_MESS
    @Test
    void editProfile_post_validationError_shouldShowFormWithError() throws Exception {
        mockMvc.perform(post("/editProfile")
                        .sessionAttr("loggedInUser", sessionUser)
                        .param("username", "")             // lỗi: username trống
                        .param("phone", "0123456789")
                        .param("email", "alice@example.com")
                        .param("address", "Hanoi"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PROFILE))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }

    // TC4: POST /editProfile, session hết hạn (sessionUser null) → trả về form với ERROR_MESS
    @Test
    void editProfile_post_noSession_shouldShowFormWithError() throws Exception {
        mockMvc.perform(post("/editProfile")
                        // không .sessionAttr nên sessionUser == null
                        .param("username", "bob")
                        .param("phone", "0987654321")
                        .param("email", "bob@example.com")
                        .param("address", "Saigon"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PROFILE))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }

    // TC5: POST /editProfile, username đã tồn tại của người khác → trả về form với ERROR_MESS
    @Test
    void editProfile_post_usernameCollision_shouldShowFormWithError() throws Exception {
        // Giả lập sessionUser
        User other = new User(); other.setId(2L); other.setUsername("alice");
        when(userService.findByUsername("alice")).thenReturn(other);

        mockMvc.perform(post("/editProfile")
                        .sessionAttr("loggedInUser", sessionUser)
                        .param("username", "alice")  // trùng với username của otherUser
                        .param("phone", "0123456789")
                        .param("email", "alice2@example.com")
                        .param("address", "Hue"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PROFILE))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }

    // TC6: POST /editProfile, thành công → gọi saveUser và trả về form với SUCCESS_MESS
    @Test
    void editProfile_post_success_shouldSaveAndShowSuccess() throws Exception {
        // Giả lập không có user khác cùng tên
        when(userService.findByUsername("aliceNew")).thenReturn(null);

        mockMvc.perform(post("/editProfile")
                        .sessionAttr("loggedInUser", sessionUser)
                        .param("username", "aliceNew")
                        .param("phone", "0123000000")
                        .param("email", "aliceNew@example.com")
                        .param("address", "Danang"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PROFILE))
                .andExpect(model().attributeExists(Message.SUCCESS_MESS));

        // verify service được gọi với sessionUser đã được cập nhật
        verify(userService).saveUser(argThat(u ->
                u.getId().equals(1L)
                        && "aliceNew".equals(u.getUsername())
                        && "0123000000".equals(u.getPhone())
                        && "aliceNew@example.com".equals(u.getEmail())
                        && "Danang".equals(u.getAddress())
        ));
    }
}