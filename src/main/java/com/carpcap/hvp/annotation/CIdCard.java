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
 * 身份号码校验注解
 * <p>默认校验中国身份证号，支持按 region 切换国家/地区。</p>
 *
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(CIdCard.List.class)
public @interface CIdCard {

    String message() default "{com.carpcap.hvp.annotation.CIdCard.message}";

    /**
     * 自定义正则表达式，配置后优先于 region
     * @return 正则表达式
     */
    String regexp() default "";

    /**
     * 身份号码所属国家/地区
     * <p>支持 CN、US、JP、KR、UK，不区分大小写并忽略首尾空格。</p>
     * <p>不支持的地区会抛出 {@link javax.validation.ConstraintDeclarationException}。</p>
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
     * @see CIdCard
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CIdCard[] value();
    }
}
