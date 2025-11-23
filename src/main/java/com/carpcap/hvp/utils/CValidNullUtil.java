package com.carpcap.hvp.utils;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidatorContext;
import java.io.File;

/**
 * @author CarpCap
 * @since 2025/11/23 22:20
 */
public class CValidNullUtil {

    /**
     * 判空
     *
     * @param obj
     * @param context
     * @return 1成功 不需要后续验证  0需要后续验证   -1失败
     * @author CarpCap
     * @since 2025/11/23 22:21
     */
    public static int validNull(Object obj, ConstraintValidatorContext context) {
        ConstraintValidatorContextImpl cvc = (ConstraintValidatorContextImpl) context;
        String allowNullStr = cvc.getConstraintDescriptor().getAttributes().get("allowNull").toString();
        boolean allowNull = true;
        if (allowNullStr.equals("false")) {
            allowNull = false;
        }

        return allowNullValid(allowNull, obj);
    }




    public static int allowNullValid(boolean allowNull, Object obj) {
        if (obj == null || obj.toString().trim().isEmpty()) {
           if (allowNull) {
               return 1;
           }else {
               return -1;
           }
        }

        //内容并非空值 需要后续的校验
        return 0;

    }

    public static int allowNullValid(boolean allowNull, File file) {
        if (file == null || !file.exists()) {
            if (allowNull) {
                return 1;
            }else {
                return -1;
            }
        }

        //内容并非空值 需要后续的校验
        return 0;

    }


}
