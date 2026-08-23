package com.carpcap.hvp.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 邮箱格式验证注解。
 * <p>
 * 支持空值控制、域名黑白名单和域名层级限制。
 * 名单中的域名会同时匹配自身及其子域名，例如 gmail.com 也会匹配 ee.gmail.com。
 *
 * @author CarpCap
 */
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(CEmail.List.class)
public @interface CEmail {

    /**
     * 校验失败时的提示消息。
     *
     * @return 提示消息或国际化消息键
     */
    String message() default "{com.carpcap.hvp.annotation.CEmail.message}";

    /**
     * 是否允许 null、空字符串或仅包含空白字符的值，默认允许。
     *
     * @return true 允许，false 不允许
     */
    boolean allowNull() default true;

    /**
     * 域名名单的使用方式，默认不启用名单限制。
     *
     * @return 名单模式
     */
    ListMode listMode() default ListMode.NONE;

    /**
     * 黑名单或白名单使用的域名数组，具体作用由 {@link #listMode()} 决定。
     * 比如  qq.com,gmail.com
     * <p>
     * 域名匹配忽略大小写，并包含指定域名的所有子域名。
     *
     * @return 域名数组
     */
    String[] domains() default {};

    /**
     * 允许的最大域名层级，按域名中的点号数量计算，默认不限制。
     * <p>
     * -1：不限制；
     * 0：允许 com；
     * 1：允许 outlook.com；
     * 2：允许 eeo.outlook.com也允许outlook.com。
     * 设置上限后，也允许低于该层级的域名.
     *
     * @return 最大子域层级
     */
    int level() default -1;

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

    /**
     * 域名名单的三种工作模式。
     */
    enum ListMode {
        /**
         * 拒绝名单中的域名及其子域名。
         */
        BLACKLIST,

        /**
         * 不启用域名名单限制。
         */
        NONE,

        /**
         * 仅允许名单中的域名及其子域名。
         */
        WHITELIST
    }

    /**
     * 可重复标注时使用的注解容器。
     *
     * @see CEmail
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        /**
         * 邮箱约束集合。
         *
         * @return 邮箱约束数组
         */
        CEmail[] value();
    }
}
