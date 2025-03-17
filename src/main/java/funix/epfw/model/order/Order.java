package funix.epfw.model.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate orderDate;
   
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    @Min(value=5,message = "Địa chỉ có ít nhất 5 ký tự.")
    private String address;

    @Column(nullable = false)
    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại bắt đầu bằng 0 và có 10 số.")
    private String phone;
    
    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String note;

    //Gia mong muốn
    @Column(nullable = false)
    private Double expectedPrice;

    @Column(nullable = false)
    private String orderType;
    //PRODUCE hoặc TOUR

    @ManyToOne
    @JoinColumn
    private Product product;

    @ManyToOne
    @JoinColumn
    private Tour tour;


    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDate.now();
        this.orderStatus = OrderStatus.PENDING;


    }

}
