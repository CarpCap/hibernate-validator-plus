package com.carpcap.hvp.constraintvalidators;


import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

/**
 * Date Type Date Validator
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CFileValidator extends AbstractCFileValidator<File> {


    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        if (file==null) return false;
        if (file.length()==0) return false;
        boolean validFileNameSuffixResult = validFileNameSuffix(file.getName());
        boolean validFileSizeResult  = file.length()<=fileSize;
        return validFileNameSuffixResult && validFileSizeResult;
    }


}
