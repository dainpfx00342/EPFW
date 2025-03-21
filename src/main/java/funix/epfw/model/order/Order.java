package funix.epfw.model.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable=false)
    @Size(min=2, message = "Tên người đặt không được ít hơn 2 ký tự")
    @Pattern(regexp = "^[a-zA-Z-ZÀ-Ỹà-ỹ\\s]*$", message = "Tên không được chứa ký tự đặc biệt hoặc số")
    private String buyerName;
   
    @Column(nullable = false)
    @Size(min=5,message = "Địa chỉ có ít nhất 5 ký tự.")
    private String address;

    @Column(nullable = false)
    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại bắt đầu bằng 0 và có 10 số.")
    private String phone;

    @Column
    @Email(message = "Email phải đúng định dạng")
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
    @Min(value = 1000 ,message = "Giá mong đợi không thể thấp hơn 1000 VNĐ")
    private int expectedPrice;

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
    private List<Product> products = new ArrayList<>();


    @ManyToMany(mappedBy = "orders",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Tour> tours = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDate.now();
        this.orderStatus = OrderStatus.PENDING;


    }

}
