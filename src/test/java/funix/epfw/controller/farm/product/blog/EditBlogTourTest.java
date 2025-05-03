package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditBlogTourTest {

    @Mock
    private BlogService blogService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private EditBlog editBlog;

    private final Long blogId = 1L;

    // 1. GET: Auth bị chặn
    @Test
    void test_editBlogGET_accessDenied() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    // 2. GET: Blog không tồn tại
    @Test
    void test_editBlogGET_blogNotFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(blogService.findById(blogId)).thenReturn(null);

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlog", view);
        }
    }

    // 3. GET: Thành công
    @Test
    void test_editBlogGET_success() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog blog = new Blog();
            blog.setProducts(List.of(new Product()));

            when(blogService.findById(blogId)).thenReturn(blog);

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("farm/product/blog/editBlog", view);
        }
    }

    // 4. POST: Auth bị chặn
    @Test
    void test_editBlogPOST_accessDenied() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editBlog.editBlog(blogId, new Blog(), session, model);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    // 5. POST: Blog không tồn tại
    @Test
    void test_editBlogPOST_blogNotFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(blogService.findById(blogId)).thenReturn(null);

            String view = editBlog.editBlog(blogId, new Blog(), session, model);
            assertEquals("redirect:/farm/product/blog/manageBlog", view);
        }
    }

    // 6. POST: Thành công
    @Test
    void test_editBlogPOST_success() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog existing = new Blog();
            existing.setProducts(List.of(new Product()));
            existing.getProducts().getFirst().setId(3L);

            Blog incoming = new Blog();
            incoming.setTitle("New Title");
            incoming.setContent("New Content");

            when(blogService.findById(blogId)).thenReturn(existing);

            String view = editBlog.editBlog(blogId, incoming, session, model);
            assertEquals("redirect:/manageBlog/product/3", view);
            verify(blogService).saveBlog(existing);
        }
    }
}