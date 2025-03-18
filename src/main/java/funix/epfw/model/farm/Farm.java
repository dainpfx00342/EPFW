package funix.epfw.model.farm;

import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, message = "Tên trang trại phải có ít nhất 5 ký tự")
    private String farmName;

    @Column(nullable = false)
    @Size(min = 5, message = "Địa chỉ trang trại phải có ít nhất 5 ký tự")
    private String address;

    @Column(nullable = false)
    @Size(min = 5, message = "Mô tả trang trại phải có ít nhất 5 ký tự")
    private String description;

    @Column(nullable = false)
    @Pattern(regexp = "0[0-9]{9}", message = "Số điện thoại không hợp lệ")
    private String contact;

    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy ="farm",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product> products;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Tour> tours;


}
