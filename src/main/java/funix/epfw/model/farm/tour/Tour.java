package funix.epfw.model.farm.tour;

import funix.epfw.constants.TourStatus;
import funix.epfw.constants.TourType;
import funix.epfw.model.farm.Farm;
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
@Table
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min=3,message="Tên chuyến đi không được ít hơn 3 ký tự")
    private String tourName;

    @Column(nullable = false)
    @Size(min = 5,message = "Mô tả về chuyến du lịch không được ít hơn 5 ký tự")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(nullable = false)
    @Min(value = 1,message ="Số người đăng ký tối thiểu là 1 người, tối đa là 100")
    private int guestNo; // Số người đăng ký

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTourDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message="Ngày kết thúc phải lớn hơn ngày hiện tại")
    private LocalDate endTourDate;

    @Column
    @Min(value=1, message ="Thời gian tham quan tối thiểu 1 ngày/chuyến")
    @Max(value=30, message="Thời gian tham quan tối đa là 1 tháng/chuyến")
    private int timeDuration;

    @Column
    @Min(value=0,message="Giá vé tối thiểu = 0 (free)")
    private int ticketPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TourStatus status = TourStatus.PENDING; // Trạng thái tour

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

   }
