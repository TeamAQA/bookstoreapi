package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

public class ZipCodeValidator implements ConstraintValidator<ValidZipCode, String> {
    private static final String TEXT_PATTERN = "^[0-9]{2}-[0-9]{3}$";

    @Override
    public void initialize(ValidZipCode constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNullOrEmpty(value)) {
            return false;
        }
        return value.matches(TEXT_PATTERN);
    }
}
