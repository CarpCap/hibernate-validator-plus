package com.carpcap.validatorplus.constraintvalidators;


import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;
import java.util.Map;

/**
 * Date Type Date Validator
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CDateValidator extends AbstractCDateValidator<Date> {



    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        ConstraintValidatorContextImpl cvc=(ConstraintValidatorContextImpl) context;
        Map<String, Object> attributes = cvc.getConstraintDescriptor().getAttributes();
        String max=attributes.get("max").toString();
        String min=attributes.get("min").toString();
        String format=attributes.get("format").toString();
        return isValid(date,max,min,format);
    }
}
