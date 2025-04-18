package funix.epfw.model.farm.product;

import funix.epfw.constants.Category;
import funix.epfw.constants.Unit;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Size(min = 5, message = "Tên sản phẩm phải có ít nhất 5 ký tự")
    private String productName;

    @Column(nullable = false)
    @Min(value = 0, message="Số tồn kho không thể là số âm")
    private int numberOfStock;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    @Min(value=1000, message="Giá sản phẩm không thể nhỏ hơn 1000 VNĐ")
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category productCategory;

    @Column
    private LocalDateTime createdTimes;

    @Column
    private LocalDateTime updatedTimes;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "farm_id")
   @ToString.Exclude
   private Farm farm;

   @ManyToMany
   @JoinTable(
           name = "product_has_orders",
           joinColumns = @JoinColumn(name = "product_id"),
           inverseJoinColumns = @JoinColumn(name = "orders_id")
   )
   @ToString.Exclude
   private List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_has_blog",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "blog_id")
    )
    @ToString.Exclude
    private List<Blog> blogs = new ArrayList<>();


   @PrePersist
    protected void onCreate() {
        this.createdTimes = LocalDateTime.now();
        this.status = true;

    }

}
