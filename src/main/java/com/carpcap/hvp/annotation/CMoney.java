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
 * 金额格式验证注解
 * 支持验证数字、字符串或BigDecimal类型的金额格式
 * 
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CMoney.List.class)
public @interface CMoney {

    String message() default "{com.carpcap.hvp.annotation.CMoney.message}";

    /**
     * 最小金额值（包含），默认为0
     * @return 最小金额值
     */
    double min() default 0;

    /**
     * 最大金额值（包含），默认为Double.MAX_VALUE
     * @return 最大金额值
     */
    double max() default Double.MAX_VALUE;


    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;


    /**
     * 最小整数位数（包含），默认为1 ， 1代表0-9   2代表 0-99
     * @return 最小整数位数
     */
    int minIntegerDigits() default 1;

    /**
     * 最大整数位数（包含），默认为Integer.MAX_VALUE
     * @return 最大整数位数
     */
    int maxIntegerDigits() default Integer.MAX_VALUE;

    /**
     * 小数位数（包含），默认为2
     * @return 小数位数
     */
    int decimalPlaces() default 2;

    /**
     * 是否允许省略小数点和小数部分，默认为true
     * @return true允许省略小数点和小数部分，false不允许
     */
    boolean allowNoDecimal() default true;

    /**
     * 是否允许前导零，默认为false
     * @return true允许前导零，false不允许
     */
    boolean allowLeadingZero() default false;

    /**
     * 是否允许货币符号（如$、¥），默认为true
     * @return true允许货币符号，false不允许
     */
    boolean allowCurrencySymbol() default true;

    /**
     * 允许的货币符号列表，默认为空表示允许任何货币符号
     * @return 允许的货币符号数组
     */
    String[] allowedCurrencySymbols() default { };

    /**
     * 是否允许千分位分隔符，默认为true
     * @return true允许千分位分隔符，false不允许
     */
    boolean allowThousandSeparator() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @see CMoney
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CMoney[] value();
    }
}
