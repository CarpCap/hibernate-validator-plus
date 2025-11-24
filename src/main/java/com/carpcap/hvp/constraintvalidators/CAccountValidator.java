package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CAccount;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

/**
 * 账户名验证器<br>
 * 支持自定义账户名长度范围验证
 * 
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CAccountValidator extends AbstractCPatternValidator<CAccount> {
    
    // 账户最小长度 包含
    private int min;
    // 账户最大长度 包含
    private int max;
    
    @Override
    public void initialize(CAccount constraintAnnotation) {
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


