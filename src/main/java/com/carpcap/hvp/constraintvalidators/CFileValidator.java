package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CFile;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

/**
 * 文件验证器，File格式
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CFileValidator extends AbstractCFileValidator<File> {


    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        int vn = CValidNullUtil.allowNullValid(allowNull,file);
        if (0 != vn) {
            return vn == 1;
        }

        boolean validFileNameSuffixResult = validFileNameSuffix(file.getName());
        boolean validFileSizeResult  = file.length()<=fileSize;
        return validFileNameSuffixResult && validFileSizeResult;
    }


}
