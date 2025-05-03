package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.product.blog.BlogService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class DeleteBlogTest {

    @Mock
    private BlogService blogService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private DeleteBlog deleteBlog;


    private final Long blogId = 1L;
    private final Long productId = 2L;

    @Test
    void test_deleteBlog_AccessDenied() {
        try (MockedStatic<AuthUtil> sessionMock = Mockito.mockStatic(AuthUtil.class)) {
            sessionMock.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = deleteBlog.deleteBlog(blogId, productId, session, model);
            assertEquals("redirect:/accessDenied", view);

        }
    }

    @Test
    void test_deleteBlog_blogNotFound() {
        try (MockedStatic<AuthUtil> sessionMock = Mockito.mockStatic(AuthUtil.class)) {
            sessionMock.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            // Giả lập không tìm thấy blog
            when(blogService.findById(blogId)).thenReturn(null);

            String view = deleteBlog.deleteBlog(blogId, productId, session, model);

            assertEquals("redirect:/manageBlog/" + productId, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Không tìm thấy blog cần xóa hoặc blog không tồn tại"));
        }
    }

    @Test
    void test_deleteBlog_Success(){
            try(MockedStatic<AuthUtil> sessionMock = Mockito.mockStatic(AuthUtil.class)) {
                sessionMock.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

                String view = deleteBlog.deleteBlog(blogId, productId, session, model);
                assertEquals("redirect:/manageBlog/" + productId, view);
            }
        }
}

