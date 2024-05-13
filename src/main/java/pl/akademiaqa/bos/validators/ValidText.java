package pl.akademiaqa.bos.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TextValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidText {
    String message() default "must contain only letters, spaces or -";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

