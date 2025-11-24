package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 文件验证器、Str格式
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CFileStrValidator extends AbstractCFileValidator<String> {



    @Override
    public boolean isValid(String fileName, ConstraintValidatorContext constraintValidatorContext) {
        int vn = CValidNullUtil.validNull(fileName, constraintValidatorContext);
        if (0 != vn) {
            return vn == 1;
        }

        return super.validFileNameSuffix(fileName);
    }


}
