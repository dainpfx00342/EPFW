package funix.epfw.ITC.controller.user;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EditUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(42L);
        existingUser.setUsername("john");
        existingUser.setPassword("password");
        existingUser.setPhone("0123456789");
        existingUser.setEmail("john@example.com");
        existingUser.setAddress("Hanoi");
    }


    @Test
    void showEditForm_notAdmin_shouldRedirect() throws Exception {
                 mockMvc.perform(get("/editUser/42")
                            .sessionAttr("loggedInUser", existingUser))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));

    }


    @Test
    void showEditForm_userNotFound_shouldFlashErrorAndRedirect() throws Exception {
        existingUser.setRole(Role.ADMIN);
        when(userService.findById(42L)).thenReturn(null);

        mockMvc.perform(get("/editUser/42")
                        .sessionAttr("loggedInUser", existingUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(Message.ERROR_MESS))
                .andExpect(redirectedUrl("/manageUser"));
    }


    @Test
    void showEditForm_userFound_shouldShowEditForm() throws Exception {
       existingUser.setRole(Role.ADMIN);
        when(userService.findById(42L)).thenReturn(existingUser);

        mockMvc.perform(get("/editUser/42")
                        .sessionAttr("loggedInUser", existingUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_USER))
                .andExpect(model().attribute("user", existingUser));
    }


    @Test
    void editUser_userNotFound_shouldFlashErrorAndRedirect() throws Exception {
        existingUser.setRole(Role.ADMIN);
        when(userService.findById(42L)).thenReturn(null);

        mockMvc.perform(post("/editUser/42")
                        .sessionAttr("loggedInUser", existingUser)
                        .param("username", "new")
                        .param("phone", "0999999999")
                        .param("email", "new@example.com")
                        .param("address", "Saigon"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(Message.ERROR_MESS))
                .andExpect(redirectedUrl("/manageUser"));
    }


    @Test
    void editUser_validUpdate_shouldSaveAndRedirect() throws Exception {
        existingUser.setRole(Role.ADMIN);
        when(userService.findById(42L)).thenReturn(existingUser);

        mockMvc.perform(post("/editUser/42")
                        .sessionAttr("loggedInUser", existingUser)
                        .param("username", "NguyenVanA")
                        .param("phone", "0987654321")
                        .param("role", "ADMIN")
                        .param("email", "nva@example.com")
                        .param("address", "Danang"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(Message.SUCCESS_MESS))
                .andExpect(redirectedUrl("/manageUser"));

        // Kiểm tra saveUser được gọi với cùng object existingUser đã được cập nhật
        verify(userService).saveUser(argThat(u ->
                u.getId().equals(42L)
                        && "NguyenVanA".equals(u.getUsername())
                        && "0987654321".equals(u.getPhone())
                        && Role.ADMIN.equals(u.getRole())
                        && "nva@example.com".equals(u.getEmail())
                        && "Danang".equals(u.getAddress())
        ));
    }

}