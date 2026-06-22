package com.carpcap.hvp.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CPhone.List.class)
public @interface CPhone {

    String message() default "{com.carpcap.hvp.annotation.CPhone.message}";

    String regexp() default "^1(?:3[0-9]|4[01456789]|5[0-9]|66|7[0-9]|8[0-9]|9[0-9])\\d{8}$";



    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;



    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


    /**
     *
     * @see CPhone
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CPhone[] value();
    }
}
