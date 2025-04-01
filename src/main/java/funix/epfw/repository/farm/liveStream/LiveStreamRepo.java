package funix.epfw.repository.farm.liveStream;

import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LiveStreamRepo extends JpaRepository<LiveStream, Long> {
    List<LiveStream> findByFarmIdIn(Collection<Long> farmIds);
}
