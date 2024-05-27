package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsMinOrMax.isBelowMinOrAboveMax;
import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String TEXT_PATTERN = "^(?!.*\\.\\.)[\\w!#$%&'*+/=?`{|}~^.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNullOrEmpty(value)) {
            return false;
        }
        if (isBelowMinOrAboveMax(value)) {
            return false;
        }
        return value.matches(TEXT_PATTERN);
    }
}
