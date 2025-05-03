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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditBlogTest {

    @Mock
    private BlogService blogService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private EditBlog editBlog;

    private final Long blogId = 1L;

    @Test
    void test_editBlog_accessDenied(){
        try(MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(()-> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_editBlogGet_blogNotFound(){
        try(MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(()-> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(blogService.findById(blogId)).thenReturn(null);

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlog", view);
        }
    }

    @Test
    void test_editBlogGet_success(){
        try(MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(()-> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog blog = new Blog();
            when(blogService.findById(blogId)).thenReturn(blog);

            String view = editBlog.editBlog(blogId, model, session);
            assertEquals("farm/product/blog/editBlog", view);
        }
    }

    @Test
    void test_editBlogPOST_accessDenied() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editBlog.editBlog(blogId, new Blog(), session, model);
            assertEquals("redirect:/accessDenied", view);
        }
    }

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

    @Test
    void test_editBlogPOST_blogNotFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(blogService.findById(blogId)).thenReturn(null);

            String view = editBlog.editBlog(blogId, new Blog(), session, model);
            assertEquals("redirect:/farm/product/blog/manageBlog", view);
        }
    }
}