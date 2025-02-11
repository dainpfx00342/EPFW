package funix.epfw.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {PasswordMatchesValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Password and confirm password must match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}