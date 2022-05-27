package com.kwon.validatorplus.utils;

import javax.validation.*;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * Manually Validate Toolbox
 * @author Kwon
 */

public class ValidatorUtil {

    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> set = validator.validate(object);
        for (ConstraintViolation constraintViolation : set) {
            throw new ValidationException(constraintViolation.getMessage());
        }
    }

    public static void validate(Object object,Class<?>... groups){
        //TODO 2022/5/26 11:44 Kwon  validate method returnValue all property   whether update one data
        Set<ConstraintViolation<Object>> set = validator.validate(object,groups);
        for (ConstraintViolation constraintViolation : set) {
            throw new ValidationException(constraintViolation.getMessage());
        }
    }

    public static void validateProperty(Object object,String propertyName,Class<?>... groups){
        Set<ConstraintViolation<Object>> set = validator.validateProperty(object,propertyName,groups);
        for (ConstraintViolation constraintViolation : set) {
            throw new ValidationException(constraintViolation.getMessage());
        }
    }

}
