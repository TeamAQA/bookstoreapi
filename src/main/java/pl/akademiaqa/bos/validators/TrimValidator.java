package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TrimValidator implements ConstraintValidator<ValidTrim, String> {

    private static final String TEXT_PATTERN = "^(?!\\s).*(?<!\\s)$";

    @Override
    public void initialize(ValidTrim constraintAnnotation) {
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
