package funix.epfw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

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
    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0")
    private int numberOfStock;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdTimes;

    @PrePersist
    protected void onCreate() {
        this.createdTimes = LocalDateTime.now();
        this.status = true;
    }


}
