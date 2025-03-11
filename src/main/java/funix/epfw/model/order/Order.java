package funix.epfw.model.order;

import funix.epfw.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderDate;

    @Column(nullable = false)
    private String deliveryDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String totalPrice;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String paymentStatus;

    @Column(nullable = false)
    private String deliveryStatus;

    @Column(nullable = false)
    private String deliveryMethod;

    @Column(nullable = false)
    private String deliveryPrice;

    @Column(nullable = false)
    private String note;


}
