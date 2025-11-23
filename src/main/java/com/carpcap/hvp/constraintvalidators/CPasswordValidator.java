package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CAccount;
import com.carpcap.hvp.annotation.CPassword;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPasswordValidator extends AbstractCPatternValidator<CPassword> {
    // 账户最小长度 包含
    private int min;
    // 账户最大长度 包含
    private int max;

    @Override
    public void initialize(CPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }


    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // 空值检查由@NotNull等注解处理
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }


        String valueStr = value.toString();

        // 长度验证
        if (valueStr.length() < min) {
            return false;
        }

        if (valueStr.length() > max) {
            return false;
        }

        // 调用父类的正则验证
        return super.isValid(value, context);
    }


}


