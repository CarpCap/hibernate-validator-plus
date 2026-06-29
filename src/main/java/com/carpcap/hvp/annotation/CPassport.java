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
 * 护照号校验注解
 * <p>默认中国格式校验，支持自定义region切换不同国家的护照格式</p>
 *
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CPassport.List.class)
public @interface CPassport {

    String message() default "{com.carpcap.hvp.annotation.CPassport.message}";

    String regexp() default "";

    /**
     * 护照所属国家/地区
     * <p>可选值: CN, US, JP, UK, KR 等，每个 region 对应独立的护照号验证策略</p>
     * <p>当指定 region 时，将使用该地区对应的护照号正则进行验证；</p>
     * <p>当同时指定了 regexp，则 regexp 优先级更高</p>
     * @return 地区代码
     */
    String region() default "CN";

    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @see CPassport
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CPassport[] value();
    }
}
