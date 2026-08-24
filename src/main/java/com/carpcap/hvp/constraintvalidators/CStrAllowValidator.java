package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CStrAllow;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 校验字符串值是否属于允许集合。 */
@AutoService(ConstraintValidator.class)
public class CStrAllowValidator implements ConstraintValidator<CStrAllow, String> {
    private Set<String> allowedValues;

    @Override
    public void initialize(CStrAllow annotation) {
        if (annotation.value().length == 0) {
            throw new ConstraintDeclarationException("CStrAllow.value 不能为空");
        }
        allowedValues = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(annotation.value())));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int nullResult = CValidNullUtil.validNull(value, context);
        return nullResult != 0 ? nullResult == 1 : allowedValues.contains(value);
    }
}
