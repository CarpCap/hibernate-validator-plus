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
@Repeatable(CDomain.List.class)
public @interface CDomain {

    String message() default "{com.carpcap.hvp.annotation.CDomain.message}";

    /**
     * 允许的最大域名层级，按域名中的点号数量计算，默认不限制。
     * <p>
     * -1：不限制；
     * 0：允许顶级域名 com，但需要allowTld=true；
     * 1：允许1级域名 outlook.com；
     * 2：允许2级域名 eeo.outlook.com 也允许outlook.com。
     * 设置上限后，也允许低于该层级的域名.
     *
     * @return 最大子域层级
     */
    int level() default -1;

    /** 是否允许只有一个标签的顶级域名，例如 com，默认不允许。 */
    boolean allowTld() default false;

    /**
     * 是否允许null值
     * @return true允许null，false不允许null
     */
    boolean allowNull() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



    /**
     *
     * @see CDomain
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        CDomain[] value();
    }
}
