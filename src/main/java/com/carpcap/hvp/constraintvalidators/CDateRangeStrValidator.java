package com.carpcap.hvp.constraintvalidators;


import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;
import java.util.Map;

/**
 * String Type Date Validator
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CDateRangeStrValidator extends AbstractCDateRangeValidator<CharSequence> {


    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        ConstraintValidatorContextImpl cvc = (ConstraintValidatorContextImpl) context;
        Map<String, Object> attributes = cvc.getConstraintDescriptor().getAttributes();
        String format = attributes.get("format").toString();
        String min = attributes.get("min").toString();
        String max = attributes.get("max").toString();

        Date value;
        if (charSequence == null) {
            value = null;
        } else {
            value = super.stringToDate(charSequence.toString(), format);

        }
        return isValid(value, max, min, format);
    }
}


