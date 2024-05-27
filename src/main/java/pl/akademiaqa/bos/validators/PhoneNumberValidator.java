package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static pl.akademiaqa.bos.commons.IsMinOrMax.isBelowMinOrAboveMax;
import static pl.akademiaqa.bos.commons.IsNullOrEmpty.isNullOrEmpty;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String PHONE_NUMBER_PATTERN = "^(?!\\s)(\\d{3}-?\\d{3}-?\\d{3})(?!\\s)$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (isNullOrEmpty(phoneNumber)) {
            return false;
        }
        if (isBelowMinOrAboveMax(phoneNumber)) {
            return false;
        }
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}

