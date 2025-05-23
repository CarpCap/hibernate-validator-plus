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
@Repeatable(IAccount.List.class)
public @interface IAccount {

    String message() default "账号不合法 字母开头，允许5-16字节，允许字母数字下划线";

    String regexp() default "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";


    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     * @see IAccount
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        IAccount[] value();
    }
}
