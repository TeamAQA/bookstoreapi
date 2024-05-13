package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TextValidator implements ConstraintValidator<ValidText, String> {

    private static final String TEXT_PATTERN = "^(?! )[A-Za-z-]+(?:[\\s-][A-Za-z-]+)*(?<! )$";

    @Override
    public void initialize(ValidText constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are valid, use @NotNull to validate null values
        }
        return value.matches(TEXT_PATTERN);
    }
}
