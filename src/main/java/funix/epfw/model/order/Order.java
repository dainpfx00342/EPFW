package funix.epfw.model.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.model.vote.Vote;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@DynamicUpdate
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate orderDate; //Ngày đặt hàng

    @Column(nullable=false)
    @Size(min=5, message = "Tên người đặt không được ít hơn 5 ký tự")
    @Pattern(regexp = "^[a-zA-Z-ZÀ-Ỹà-ỹ\\s]*$", message = "Tên không được chứa ký tự đặc biệt hoặc số")
    private String buyerName;
   
    @Column(nullable = false)
    @Size(min=5,message = "Địa chỉ có ít nhất 5 ký tự.")
    private String address;

    @Column(nullable = false)
    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại bắt đầu bằng 0 và có 10 số.")
    private String phone;

    @Column
   @Email(message = "Email không đúng định dạng")
    private String email;
    
    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String note;

    //Gia mong muốn
    @Column(nullable = false)
    @Min(value = 0 ,message = "Giá mong đợi không thể thấp hơn 0 VNĐ")
    private int expectedPrice;

    //Số lượng:
    @Column(nullable = false)
    @Min(value=1, message="Số lượng không thể thấp hơn 1")
    private int quantity;

    @Column(nullable = false)
    private String orderType;
    //PRODUCT hoặc TOUR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToMany
    @JoinTable(
            name = "product_has_orders",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name="tour_has_orders",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    @ToString.Exclude
    private List<Tour> tours = new ArrayList<>();

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    @ToString.Exclude
    private Blog blog;
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDate.now();
        this.orderStatus = OrderStatus.PENDING;


    }

}
