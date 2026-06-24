package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPhone;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPhoneValidator extends AbstractCPatternValidator<CPhone> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();

    static {
        REGION_PATTERNS.put("CN", "^1(?:3[0-9]|4[01456789]|5[0-9]|66|7[0-9]|8[0-9]|9[0-9])\\d{8}$");
        REGION_PATTERNS.put("US", "^(\\+?1)?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$");
        REGION_PATTERNS.put("JP", "^0[1-9]\\d{8,9}$");
        REGION_PATTERNS.put("KR", "^01[016789][-.\\s]?\\d{3,4}[-.\\s]?\\d{4}$");
        REGION_PATTERNS.put("UK", "^0[1-9]\\d{8,9}$");
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(charSequence, context);
        if (0 != vn) {
            return vn == 1;
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

        if (charSequence != null && !charSequence.toString().trim().isEmpty()) {
            return Pattern.matches(pattern, charSequence);
        }
        return true;
    }
}
