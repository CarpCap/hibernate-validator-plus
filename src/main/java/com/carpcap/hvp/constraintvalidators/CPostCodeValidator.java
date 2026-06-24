package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPostCode;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @CPostCode 校验器
 * <p>支持 CN/US/JP/UK/KR 五国邮编格式，也支持自定义正则</p>
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPostCodeValidator implements ConstraintValidator<CPostCode, CharSequence> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();

    static {
        REGION_PATTERNS.put("CN", "^\\d{6}$");
        REGION_PATTERNS.put("US", "^\\d{5}(-\\d{4})?$");
        REGION_PATTERNS.put("JP", "^\\d{3}-\\d{4}$");
        REGION_PATTERNS.put("UK", "^[A-Za-z]{1,2}\\d{1,2}[A-Za-z]?\\s?\\d[A-Za-z]{2}$");
        REGION_PATTERNS.put("KR", "^\\d{5}$");
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(charSequence, context);
        if (0 != vn) {
            return vn == 1;
        }

        String value = charSequence.toString().trim();
        if (value.isEmpty()) {
            return true;
        }

        ConstraintValidatorContextImpl cvc = (ConstraintValidatorContextImpl) context;
        String regexp = cvc.getConstraintDescriptor().getAttributes().get("regexp").toString();
        String region = cvc.getConstraintDescriptor().getAttributes().get("region").toString();

        String pattern;
        if (regexp != null && !regexp.trim().isEmpty()) {
            pattern = regexp;
        } else if (region != null && !region.trim().isEmpty()) {
            pattern = REGION_PATTERNS.get(region);
        } else {
            return true;
        }

        if (pattern == null || pattern.isEmpty()) {
            return true;
        }

        return Pattern.matches(pattern, value);
    }
}
