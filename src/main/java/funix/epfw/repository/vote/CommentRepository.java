package funix.epfw.repository.vote;

import funix.epfw.model.vote.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogId(Long blogId);
}
