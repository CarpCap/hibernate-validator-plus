package com.carpcap.validatorplus.annotation;

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
@Repeatable(IPassword.List.class)
public @interface IPassword {

    String message() default "密码不合法 以字母开头，长度在6~18之间，只能包含字母、数字和下划线";

    String regexp() default "^[a-zA-Z]\\w{5,17}$";


    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     * @see IPassword
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        IPassword[] value();
    }
}