package funix.epfw.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator <PasswordMatches, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean isValid = user.getPassword() != null && user.getPassword().equals(user.getConfirmPassword());
        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Mật khẩu và xác nhận mật khẩu phải trùng nhau")
                    .addPropertyNode("confirmPassword").addConstraintViolation();
        }
        return user.getPassword() != null && user.getPassword().equals(user.getConfirmPassword());
    }
}
