package com.carpcap.hvp.utils;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Manually Validate Toolbox
 *
 * @author CarpCap
 */

public class CValid {

    // 正常校验器
    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();


    // 快速校验器
    private static final Validator fastValidator = Validation.byDefaultProvider()
            .configure()
            .addProperty("hibernate.validator.fail_fast", "true")
            .buildValidatorFactory().getValidator();


    /**
     * 直接校验，默认规则
     * 失败 抛出异常 ValidationException
     *
     * @param object 校验对象
     * @author CarpCap
     * @since 2025/11/23 11:17
     */
    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> set = fastValidator.validate(object);
        processResult(set);
    }


    /**
     * 直接校验，默认规则
     * 失败 返回错误信息 集合
     *
     * @param object 校验对象
     * @return 失败信息 集合
     * @author CarpCap
     * @since 2025/11/23 11:51
     */
    public static List<String> tryValidate(Object object) {
        Set<ConstraintViolation<Object>> set = validator.validate(object);
        return tryProcessResult(set);
    }


    /**
     * 直接校验，默认规则
     * 快速校验，遇到一个失败结果直接返回 不进行全部校验
     * 失败 返回错误信息
     *
     * @param object 校验对象
     * @return 失败信息
     * @author CarpCap
     * @since 2025/11/23 11:51
     */
    public static String tryFastValidate(Object object) {
        Set<ConstraintViolation<Object>> set = fastValidator.validate(object);
        return tryFastProcessResult(set);
    }


    /**
     * 指定分组规则校验
     * 失败抛出异常 ValidationException
     *
     * @param object 校验对象
     * @param groups 分组
     * @author CarpCap
     * @since 2025/11/23 11:20
     */
    public static void validate(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = fastValidator.validate(object, groups);
        processResult(set);
    }

    /**
     * 指定分组规则校验
     * 失败 返回错误信息 集合
     *
     * @param object 校验对象
     * @return 失败信息 集合
     * @author CarpCap
     * @since 2025/11/23 11:51
     */
    public static List<String> tryValidate(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = validator.validate(object, groups);
        return tryProcessResult(set);
    }


    /**
     * 指定分组规则校验
     * 快速校验，遇到一个失败结果直接返回 不进行全部校验
     * 失败 返回错误信息
     *
     * @param object 校验对象
     * @return 失败信息
     * @author CarpCap
     * @since 2025/11/23 11:51
     */
    public static String tryFastValidate(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = fastValidator.validate(object, groups);
        return tryFastProcessResult(set);
    }


    /**
     * 指定某个参数+分组规则校验
     * 失败 抛出异常 ValidationException
     *
     * @param object       校验对象
     * @param propertyName 参数名称
     * @param groups       分组
     * @author CarpCap
     * @since 2025/11/23 11:20
     */
    public static void validateProperty(Object object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = fastValidator.validateProperty(object, propertyName, groups);
        processResult(set);
    }


    /**
     * 指定某个参数+分组规则校验
     * 失败 返回错误信息 集合
     *
     * @param object       校验对象
     * @param propertyName 参数名称
     * @param groups       分组
     * @author CarpCap
     * @since 2025/11/23 11:20
     */
    public static List<String> tryValidateProperty(Object object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = validator.validateProperty(object, propertyName, groups);
        return tryProcessResult(set);
    }



    /**
     * 指定某个参数+分组规则校验
     * 快速校验，遇到一个失败结果直接返回 不进行全部校验
     * 失败 返回错误信息
     * @param object       校验对象
     * @param propertyName 参数名称
     * @param groups       分组
     * @author CarpCap
     * @since 2025/11/23 11:20
     */
    public static String tryFastValidateProperty(Object object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> set = fastValidator.validateProperty(object, propertyName, groups);
        return tryFastProcessResult(set);
    }






    /**
     * 处理结果，失败抛出异常 ValidationException
     *
     * @param set
     * @author CarpCap
     * @since 2025/11/23 11:31
     */
    private static void processResult(Set<ConstraintViolation<Object>> set) {
        if (set != null && !set.isEmpty()) {
            throw new ValidationException(set.iterator().next().getMessage());
        }
    }


    /**
     * 处理结果,返回最上面的结果 不会抛出异常
     *
     * @param set
     * @return 失败信息
     * @author CarpCap
     * @since 2025/11/23 11:26
     */
    private static String tryFastProcessResult(Set<ConstraintViolation<Object>> set) {
        if (set != null && !set.isEmpty()) {
            return set.iterator().next().getMessage();
        }
        return null;
    }


    /**
     * 处理结果 将ConstraintViolation set 转为 List<String>
     *
     * @param set
     * @return 失败信息 集合
     * @author CarpCap
     * @since 2025/11/23 11:26
     */
    private static List<String> tryProcessResult(Set<ConstraintViolation<Object>> set) {
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<Object> item : set) {
            result.add(item.getMessage());
        }
        return result;
    }
}
