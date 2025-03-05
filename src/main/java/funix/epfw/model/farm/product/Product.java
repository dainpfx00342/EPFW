package funix.epfw.model.farm.product;

import funix.epfw.constants.ProductCategory;
import funix.epfw.constants.Unit;
import funix.epfw.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Size(min = 5, message = "Tên sản phẩm phải có ít nhất 5 ký tự")
    private String productName;

    @Column(nullable = false)
    @Size(max = 12, message = "Số lượng sản phẩm không thể lớn hơn hàng trăm tỉ")
    @Pattern(regexp = "^[0-9]*$", message = "Số lượng sản phẩm phải là số")
    private String numberOfStock;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    @Size(max = 12, message = "Giá sản phẩm khoảng từ 0 đến 999 tỉ")
    @Pattern(regexp = "^[0-9]*$", message = "Giá sản phẩm phải là số")
    private String price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User createdBy;

    @Column
    private LocalDateTime createdTimes;

    @Column
    private LocalDateTime updatedTimes;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn
    private List<Blog> blogs = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdTimes = LocalDateTime.now();
        this.status = true;

    }


}
