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
 * MAC地址格式验证注解
 * 支持常见的MAC地址格式：
 * - 6个字节，如: AA:BB:CC:DD:EE:FF
 * - 6个字节，使用连字符分隔，如: AA-BB-CC-DD-EE-FF
 * - 6个字节，无分隔符，如: AABBCCDDEEFF
 * 
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CMacAddress.List.class)
public @interface CMacAddress {

    String message() default "{com.carpcap.hvp.annotation.CMacAddress.message}";

    /**
     * 是否允许小写字母，默认为true
     */
    boolean allowLowercase() default true;

    /**
     * 是否允许省略前导零，默认为false
     */
    boolean allowOmittingLeadingZero() default false;

    /**
     * 是否允许IEEE EUI-64格式（8个字节），默认为false
     */
    boolean allowEui64() default false;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @see CMacAddress
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CMacAddress[] value();
    }
}