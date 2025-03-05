package funix.epfw.model.farm;

import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
    private String description;

    @Column(nullable = false)
    private String contact;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<Product> products;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<Tour> tours;
}
