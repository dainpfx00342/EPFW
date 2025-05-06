package funix.epfw.ITC.controller.auth;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LoginIntegrationTest {

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

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        mockUser.setRole(Role.BUYER);
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(post("/login")
                        .param("username", "testuser")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(post("/login")
                        .param("username", "testuser")
                        .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.LOGIN)); // dùng đúng ViewPaths nếu có
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(userService.findByUsername("nouser")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", "nouser")
                        .param("password", "whatever"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.LOGIN));
    }
}