package funix.epfw.model.farm.liveStream;

import funix.epfw.model.farm.Farm;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "livestreams")
public class LiveStream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @URL(message = "Đường dẫn không đúng định dạng, vui lòng thử lại")
    private String url;

    @Column(nullable = false)
    @Size(min = 5,message = "Tên buổi live không ít hơn 5 ký tự")
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateToLive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @PrePersist
    protected void onCreate(){
        dateToLive = LocalDateTime.now();
    }
}
