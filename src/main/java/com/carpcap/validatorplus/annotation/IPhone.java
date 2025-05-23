package com.carpcap.validatorplus.annotation;

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
@Repeatable(IPhone.List.class)
public @interface IPhone {

    String message() default "手机不合法";

    String regexp() default "^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[7])|(18[0,0-9]))\\d{8}$";


    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     * @see IPhone
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        IPhone[] value();
    }
}
