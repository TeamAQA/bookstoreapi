package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsMinOrMax.isBelowMinOrAboveMax;
import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

public class TextValidator implements ConstraintValidator<ValidText, String> {

    private static final String TEXT_PATTERN = "^(?! )[A-Za-z-]+(?:[\\s-][A-Za-z-]+)*(?<! )$";

    @Override
    public void initialize(ValidText constraintAnnotation) {
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
