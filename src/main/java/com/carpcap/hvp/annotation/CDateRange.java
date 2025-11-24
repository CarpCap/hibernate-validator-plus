package com.carpcap.hvp.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 日期范围限制
 * 参数支持格式 String、Date、LocalDate、LocalDateTime。
 * 推荐使用格式：yyyy-MM-dd HH:mm:ss
 * 日期默认支持格式："yyyy-MM-dd'T'HH:mm:ss.SSSX", "yyyy-MM-dd'T'HH:mm:ss.SSS", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd"
 * 请注意：format参数只会影响校验，并不会影响接收参数序列化相关的内容,如果需要接收不同格式类型 自行配置序列化。
 *
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(CDateRange.List.class)
public @interface CDateRange {

    String message() default "{com.carpcap.hvp.annotation.CDateRange.message}";

    /**
     * min date
     * Example 1：2022-01-01
     * Example 2：2022-01-01 00:00:00
     * Not recommended：2022-01  、 202201
     * 最短日期
     * 示例1：2022-01-01
     * 示例2：2022-01-01 00:00:00
     * 不推荐：2022-01 、 202201
     */
    String min() default "";

    /**
     * max date
     * Example 1：2022-01-01 (equal 2022-01-01 23:59:59)
     * Example 2：2022-01-01 23:59:59
     * Not recommended：2022-01  、 202201
     * 最大日期
     * 示例1：2022-01-01（等于2022-01-01 23:59:59）
     * 示例2：2022-01-01 23:59:59
     * 不推荐：2022-01 、 202201
     */
    String max() default "";

    /**
     * 日期格式
     * 如果==null或“”，则使用hutool项目DateUtil.class自动操作解析。
     * yyyyMM需要手动设置。
     * 请注意：format参数只会影响校验，并不会影响接收参数，序列化相关的内容。
     */
    String format() default "";

    /**
     * 是否允许null值
     *
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * Defines several {@code @IDate} constraints on the same element.
     *
     * @see CDateRange
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CDateRange[] value();
    }

}
