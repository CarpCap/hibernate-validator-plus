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
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CDateRange.List.class)
public @interface CDateRange {

    String message() default "{com.carpcap.hvp.annotation.CDateRange.message}";

    /**
    * min date
    * */
    String min() default "";

    /**
     * max date
     * */
    String max() default "";

    /**
     * 日期格式
     * 如果==null或“”，则使用hutool项目DateUtil.class自动操作解析。
     * yyyyMM需要手动设置。
     * 请注意：format参数只会影响校验，并不会影响接收参数，序列化相关的内容。
     * */
    String format() default "";

    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



    /**
     * Defines several {@code @IDate} constraints on the same element.
     *
     * @see CDateRange
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CDateRange[] value();
    }

}
