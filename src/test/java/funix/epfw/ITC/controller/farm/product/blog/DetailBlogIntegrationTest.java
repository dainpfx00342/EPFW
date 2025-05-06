package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.model.vote.Comment;
import funix.epfw.model.vote.Vote;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.vote.CommentService;
import funix.epfw.service.vote.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DetailBlogIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public BlogService blogService() {
            return Mockito.mock(BlogService.class);
        }

        @Bean
        public VoteService voteService() {
            return Mockito.mock(VoteService.class);
        }

        @Bean
        public CommentService commentService() {
            return Mockito.mock(CommentService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private CommentService commentService;

    private Blog blog;
    private User user;
    private Farm farm;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("farmer1");
        user.setRole(Role.FARMER);

        farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Farm mẫu");
        farm.setUser(user);



        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Blog mẫu");
    }

    @Test
    void testDetailBlog_ProductBlog_Success() throws Exception {
        Product product = new Product();
        product.setId(10L);
        product.setFarm(farm);
        blog.setProducts(List.of(product));

        // Tạo mock đầy đủ cho vote -> order -> user
        User voteUser = new User();
        voteUser.setId(2L);
        voteUser.setUsername("testuser1");


        Order order = new Order();
        order.setUser(voteUser);

        Vote vote = new Vote();
        vote.setOrder(order);

        Comment comment = new Comment();
        comment.setUser(voteUser);

        when(blogService.findById(1L)).thenReturn(blog);
        when(voteService.findByBlogId(1L)).thenReturn(List.of(vote));
        when(commentService.findByBlogAndParentNull(blog)).thenReturn(List.of(comment));

        mockMvc.perform(get("/blogDetail/1").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.DETAIL_BLOG))
                .andExpect(model().attributeExists("blog", "product", "productId", "votes", "comments", "comment", "reply"));
    }

    @Test
    void testDetailBlog_TourBlog_Success() throws Exception {
        Tour tour = new Tour();
        tour.setId(20L);
        tour.setFarm(farm);
        blog.setTours(List.of(tour));

        User voteUser = new User();
        voteUser.setId(2L);
        voteUser.setUsername("testuser2");

        Order order = new Order();
        order.setUser(voteUser);

        Vote vote = new Vote();
        vote.setOrder(order);

        Comment comment = new Comment();
        comment.setUser(voteUser);

        when(blogService.findById(1L)).thenReturn(blog);
        when(voteService.findByBlogId(1L)).thenReturn(List.of(vote));
        when(commentService.findByBlogAndParentNull(blog)).thenReturn(List.of(comment));

        mockMvc.perform(get("/blogDetail/1").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.DETAIL_BLOG))
                .andExpect(model().attributeExists("blog", "tour", "tourId", "votes", "comments", "comment", "reply"));
    }

    @Test
    void testDetailBlog_BlogNotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/blogDetail/999").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.HOME))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }
}