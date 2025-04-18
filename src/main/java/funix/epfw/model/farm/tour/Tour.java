package funix.epfw.model.farm.tour;

import funix.epfw.constants.TourStatus;
import funix.epfw.constants.TourType;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min=5,message="Tên chuyến đi không được ít hơn 5 ký tự")
    private String tourName;

    @Column(nullable = false)
    @Size(min = 5,message = "Mô tả về chuyến du lịch không được ít hơn 5 ký tự")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt; // Ngày tạo tour

    @Column
    @Min(value=0,message="Giá vé tối thiểu = 0 (free)")
    private int ticketPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TourStatus tourStatus; // Trạng thái tour

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    @ToString.Exclude
    private Farm farm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tour_has_orders",
    joinColumns = @JoinColumn(name = "tour_id"),
    inverseJoinColumns = @JoinColumn(name = "orders_id")
    )
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tour_has_blog",
    joinColumns = @JoinColumn(name = "tour_id"),
    inverseJoinColumns = @JoinColumn(name = "blog_id")
    )
    @ToString.Exclude
    private List<Blog> blogs = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

   }
