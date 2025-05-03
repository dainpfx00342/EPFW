package funix.epfw.ITC.controller.auth;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({UserService.class})
public class RegistryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private UserService userService;

    private User newUser;

    @BeforeEach
    void setUp() {
        newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("securePass123");
        newUser.setRole(Role.BUYER);
    }

    @Test
    void testRegistrySuccess() throws Exception {
        // Giả lập: user chưa tồn tại
        when(userService.findByUsername("newuser")).thenReturn(null);

        mockMvc.perform(post("/registry")
                        .param("username", "newuser")
                        .param("password", "securePass123")
                        .param("confirmPassword", "securePass123")
                        .flashAttr("user", newUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testRegistryUsernameExists() throws Exception {
        when(userService.findByUsername("newuser")).thenReturn(newUser);

        mockMvc.perform(post("/registry")
                        .param("username", "newuser")
                        .param("password", "securePass123")
                        .param("confirmPassword", "securePass123")
                        .flashAttr("user", newUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.REGISTER));
    }

    @Test
    void testRegistryPasswordMismatch() throws Exception {
        when(userService.findByUsername("newuser")).thenReturn(null);

        mockMvc.perform(post("/registry")
                        .param("username", "newuser")
                        .param("password", "securePass123")
                        .param("confirmPassword", "wrongPass")
                        .flashAttr("user", newUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.REGISTER));
    }

    @Test
    void testRegister_Failure_HasErrors() throws Exception {
        mockMvc.perform(post("/registry")
                        .param("username", "") // username rỗng => lỗi @Size
                        .param("password", "123") // lỗi @Size
                        .param("confirmPassword", "123")
                        .param("role", "BUYER")) // đảm bảo binding đủ field
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.REGISTER))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute(Message.ERROR_MESS, "Đăng ký không thành công, vui lòng nhập lại thông tin!"));
    }
}