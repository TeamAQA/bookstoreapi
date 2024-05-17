package pl.akademiaqa.bos.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TrimValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTrim {
    String message() default "can not start or end with empty spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

