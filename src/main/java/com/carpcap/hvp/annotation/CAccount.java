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
@Repeatable(CAccount.List.class)
public @interface CAccount {

    String message() default "{com.carpcap.hvp.annotation.CAccount.message}";

    String regexp() default "^[a-zA-Z][a-zA-Z0-9_]*$";

    /**
     * 最小长度 包含
     * @return 最小长度值
     */
    int min() default 5;

    /**
     * 最大长度 包含
     * @return 最大长度值
     */
    int max() default 16;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     *
     * @see CAccount
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CAccount[] value();
    }
}
