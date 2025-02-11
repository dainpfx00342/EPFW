package funix.epfw.model;

import funix.epfw.constants.ROLE;
import jakarta.persistence.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Entity
@Data
@Validated
@Table(name = "Users")
@PasswordMatches
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 5, message = "Username phải có ít nhất 5 ký tự")
    private String username;

    @Column(nullable = false)
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ROLE role;

    @Column
    @Pattern(regexp ="0\\d{9}", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    private String phone;

    @Column
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email phải đúng định dạng")
    private String email;

    @Column
    private String address;


}
