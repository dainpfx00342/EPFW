package funix.epfw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, message = "Tên phải có ít nhất 2 ký tự")
    @Pattern(regexp = "^[a-zA-Z-ZÀ-Ỹà-ỹ\\s]*$", message = "Tên không được chứa ký tự đặc biệt hoặc số")
    private String name;

    @Column(nullable = false)
    @Email(message="Email phải đúng định dạng")
    private String email;

    @Column(nullable = false)
    @Size(min = 5, message="Message phải có ít nhất 5 ký tự")
    private String message;

    @Column(nullable = false)
   @Pattern(regexp ="0\\d{9}", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    private String phoneNumber;

}
