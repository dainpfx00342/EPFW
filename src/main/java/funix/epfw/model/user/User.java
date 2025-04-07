package funix.epfw.model.user;

import funix.epfw.constants.Role;
import funix.epfw.model.vote.Comment;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.order.Order;
import jakarta.persistence.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;


import java.util.ArrayList;
import java.util.List;

@Entity
@Data

@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 5, message = "Username phải có ít nhất 5 ký tự")
    @Pattern(regexp="^[a-zA-Z0-9]+$", message = "Username không được chứa ký tự đặc biệt")
    private String username;

    @Column(nullable = false)
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Pattern(regexp ="0\\d{9}", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    private String phone;

    @Column
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email phải đúng định dạng")
    private String email;

    @Column
    @Size(min = 5, message = "Địa chỉ phải có ít nhất 5 ký tự")
    private String address;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Farm> farms;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


}
