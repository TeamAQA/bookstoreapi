package pl.akademiaqa.bos.validators;

import pl.akademiaqa.bos.commons.IsMinOrMax;
import pl.akademiaqa.bos.commons.IsNullOrEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsMinOrMax.*;
import static pl.akademiaqa.bos.commons.IsNullOrEmpty.*;

public class StreetValidator implements ConstraintValidator<ValidStreet, String> {
    private static final String TEXT_PATTERN = "^(?! )[\\p{L}][\\p{L}\\d]*(?:[\\s-.][\\p{L}\\d]+)*(?<! )$";


    @Override
    public void initialize(ValidStreet constraintAnnotation) {
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


