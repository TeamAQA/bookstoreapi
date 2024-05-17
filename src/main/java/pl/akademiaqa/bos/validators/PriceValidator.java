package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class PriceValidator implements ConstraintValidator<ValidPrice, BigDecimal> {

    private static final String PRICE_PATTERN = "^\\d+(\\.\\d{1,2})?$";

    @Override
    public void initialize(ValidPrice constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal price, ConstraintValidatorContext context) {
        if (price == null) {
            return true; // Możesz zmienić to zachowanie w zależności od potrzeb
        }
        return String.valueOf(price).matches(PRICE_PATTERN);
    }
}

