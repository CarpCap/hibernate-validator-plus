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
 * URL格式验证注解
 * 
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CUrl.List.class)
public @interface CUrl {

    String message() default "{com.carpcap.hvp.annotation.CUrl.message}";

    /**
     * 允许的URL协议列表，默认为http和https
     */
    String[] protocols() default {"http", "https"};

    /**
     * 是否允许使用localhost or 127.0.0.1，默认为false
     */
    boolean allowLocalhost() default true;

    /**
     * 是否允许使用IP地址，默认为false
     */
    boolean allowIp() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @see CUrl
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CUrl[] value();
    }
}