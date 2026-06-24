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
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CPhone.List.class)
public @interface CPhone {

    String message() default "{com.carpcap.hvp.annotation.CPhone.message}";

    String regexp() default "";



   /**
    * 手机号码所属国家/地区
     * <p>可选值: CN, US, JP, KR, UK 等，每个 region 对应独立验证策略</p>
    * <p>当指定 region 时，将使用该地区对应的手机号正则进行验证；</p>
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
     *
     * @see CPhone
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CPhone[] value();
    }
}
