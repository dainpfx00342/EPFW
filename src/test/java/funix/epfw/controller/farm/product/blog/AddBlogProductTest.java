package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBlogProductTest {

    @InjectMocks
    private AddBlogProduct addBlogProduct;

    @Mock
    private BlogService blogService;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    private final Long productId = 1L;

    @Test
    void test_showAddBlogForm_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addBlogProduct.showAddBlogForm(model, productId, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_showAddBlogForm_productNotFound() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(productId)).thenReturn(null);

            String view = addBlogProduct.showAddBlogForm(model, productId, session);
            assertEquals(ViewPaths.ADD_BLOG, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_showAddBlogForm_success() {
        Product product = new Product();

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(productId)).thenReturn(product);

            String view = addBlogProduct.showAddBlogForm(model, productId, session);
            assertEquals(ViewPaths.ADD_BLOG, view);
            verify(model).addAttribute("product", product);
            verify(model).addAttribute(eq("blog"), any(Blog.class));
            verify(model).addAttribute("productId", productId);
        }
    }

    @Test
    void test_addBlog_productNotFound() {
        Blog blog = new Blog();
        when(productService.findById(productId)).thenReturn(null);

        String view = addBlogProduct.addBlog(blog, productId, model);
        assertEquals(ViewPaths.ADD_BLOG, view);
        verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
    }

    @Test
    void test_addBlog_success() {
        Blog blog = new Blog();
        Product product = new Product();
        product.setBlogs(new ArrayList<>());

        when(productService.findById(productId)).thenReturn(product);

        String view = addBlogProduct.addBlog(blog, productId, model);
        assertEquals("redirect:/manageBlog/product/" + productId, view);

        // Kiểm tra quan hệ 2 chiều
        assertEquals(1, blog.getProducts().size());
        assertEquals(product, blog.getProducts().getFirst());
        assertEquals(1, product.getBlogs().size());
        assertEquals(blog, product.getBlogs().getFirst());

        verify(blogService).saveBlog(blog);
    }
}