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
 * 银行卡号验证注解
 * 使用Luhn算法验证银行卡号的有效性
 * 
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CBankCard.List.class)
public @interface CBankCard {

    String message() default "{com.carpcap.hvp.annotation.CBankCard.message}";

    /**
     * 最小卡号长度，默认为13
     * @return 最小卡号长度
     */
    int minLength() default 13;

    /**
     * 最大卡号长度，默认为19
     * @return 最大卡号长度
     */
    int maxLength() default 19;

    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;


    /**
     * 是否启用Luhn算法验证，默认为true
     * @return true启用Luhn算法验证，false不启用
     */
    boolean useLuhnCheck() default true;

    /**
     * 是否允许空格分隔符，默认为true
     * @return true允许空格分隔符，false不允许
     */
    boolean allowSpaces() default true;

    /**
     * 是否允许连字符分隔符，默认为true
     * @return true允许连字符分隔符，false不允许
     */
    boolean allowHyphens() default true;

    /**
     * 允许的银行前缀列表，空表示允许任何前缀
     * @return 允许的银行前缀数组
     */
    String[] allowedPrefixes() default { };

    /**
     * 禁止的银行前缀列表，空表示不禁止任何前缀
     * @return 禁止的银行前缀数组
     */
    String[] forbiddenPrefixes() default { };

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



    /**
     * @see CBankCard
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CBankCard[] value();
    }
}
