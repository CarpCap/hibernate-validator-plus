package com.carpcap.hvp.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON 字符串格式验证注解。
 * <p>
 * 默认仅允许对象或数组，也可以限定为对象、数组或基础值。
 *
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(CJson.List.class)
public @interface CJson {

    /**
     * 校验失败时的提示消息。
     *
     * @return 提示消息或国际化消息键
     */
    String message() default "{com.carpcap.hvp.annotation.CJson.message}";

    /**
     * 是否允许 null、空字符串或仅包含空白字符的值。
     *
     * @return true 允许，false 不允许
     */
    boolean allowNull() default true;

    /**
     * JSON 根节点类型，默认仅允许对象或数组。
     *
     * @return 根节点类型
     */
    Type type() default Type.STRUCT;

    /**
     * 指定约束所属的校验分组。
     *
     * @return 校验分组
     */
    Class<?>[] groups() default {};

    /**
     * 指定约束携带的元数据。
     *
     * @return 负载类型
     */
    Class<? extends Payload>[] payload() default {};

    /** JSON 根节点类型。 */
    enum Type {
        /** 允许对象、数组及字符串、数字、布尔值和 null。 */
        ANY,
        /** 根节点必须为 JSON 对象或数组。 */
        STRUCT,
        /** 根节点必须为字符串、数字、布尔值或 null。 */
        VALUE,
        /** 根节点必须为 JSON 对象。 */
        OBJECT,
        /** 根节点必须为 JSON 数组。 */
        ARRAY
    }

    /** 可重复标注时使用的注解容器。 */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        /**
         * JSON 约束集合。
         *
         * @return JSON 约束数组
         */
        CJson[] value();
    }
}
