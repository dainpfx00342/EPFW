package funix.epfw.repository.vote;

import funix.epfw.model.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByBlogId(Long blogId);
}
