package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CStrDeny;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 校验字符串值不属于禁止集合。 */
@AutoService(ConstraintValidator.class)
public class CStrDenyValidator implements ConstraintValidator<CStrDeny, String> {
    private Set<String> deniedValues;

    @Override
    public void initialize(CStrDeny annotation) {
        if (annotation.value().length == 0) {
            throw new ConstraintDeclarationException("CStrDeny.value 不能为空");
        }
        deniedValues = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(annotation.value())));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int nullResult = CValidNullUtil.validNull(value, context);
        return nullResult != 0 ? nullResult == 1 : !deniedValues.contains(value);
    }
}
