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
@Repeatable(CIdCard.List.class)
public @interface CIdCard {

    String message() default "{com.carpcap.hvp.annotation.CIdCard.message}";

    String regexp() default "^\\d{15}|\\d{18}$";


    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;


    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



    /**
     *
     * @see CIdCard
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CIdCard[] value();
    }
}