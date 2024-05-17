package pl.akademiaqa.bos.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearMinValidator implements ConstraintValidator<YearMin, Integer> {

    private int minYear;

    @Override
    public void initialize(YearMin constraintAnnotation) {
        this.minYear = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true; // Możesz zmienić to zachowanie w zależności od potrzeb
        }
        return year >= minYear;
    }
}
