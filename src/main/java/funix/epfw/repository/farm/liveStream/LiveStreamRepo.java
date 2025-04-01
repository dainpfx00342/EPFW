package funix.epfw.repository.farm.liveStream;

import funix.epfw.model.farm.liveStream.LiveStream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveStreamRepo extends JpaRepository<LiveStream, Long> {
}
