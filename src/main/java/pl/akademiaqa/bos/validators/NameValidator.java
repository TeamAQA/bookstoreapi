package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String TEXT_PATTERN = "^(?! )[\\p{L}-]+(?:[\\s-][\\p{L}-]+)*(?<! )$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
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
