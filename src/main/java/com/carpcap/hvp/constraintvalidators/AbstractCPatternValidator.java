package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.utils.CValidNullUtil;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

/**
 *  抽象类 正则验证器
 * @author CarpCap
 */
public abstract class AbstractCPatternValidator<T extends Annotation> implements ConstraintValidator<T, CharSequence> {



    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(charSequence, context);
        if (0 != vn) {
            return vn == 1;
        }


        ConstraintValidatorContextImpl cvc=(ConstraintValidatorContextImpl) context;
        String regexp=cvc.getConstraintDescriptor().getAttributes().get("regexp").toString();
        if (charSequence != null && !charSequence.toString().trim().isEmpty()) {
            return Pattern.matches(regexp, charSequence);
        }
        return true;
    }


}
