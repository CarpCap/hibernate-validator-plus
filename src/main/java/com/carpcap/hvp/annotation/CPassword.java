package com.carpcap.hvp.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CPassword.List.class)
public @interface CPassword {

    String message() default "{com.carpcap.hvp.annotation.CPassword.message}";

    String regexp() default "^(?=.*[A-Za-z])(?=.*\\d).+$";

    /**
     * 最小长度 包含
     * @return 最小长度值
     */
    int min() default 6;

    /**
     * 最大长度 包含
     * @return 最大长度值
     */
    int max() default 18;



    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     * @see CPassword
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CPassword[] value();
    }
}