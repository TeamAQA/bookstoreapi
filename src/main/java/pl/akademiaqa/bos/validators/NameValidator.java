package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsMinOrMax.isBelowMinOrAboveMax;
import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

public class NameValidator implements ConstraintValidator<ValidName, String> {
    private static final String TEXT_PATTERN = "^[\\p{L}][\\p{L}\\s-.]*[\\p{L}]$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
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
