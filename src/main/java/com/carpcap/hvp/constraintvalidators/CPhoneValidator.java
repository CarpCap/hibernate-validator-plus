package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPhone;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintDeclarationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPhoneValidator extends AbstractCPatternValidator<CPhone> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();
    private String pattern;

    static {
        REGION_PATTERNS.put("CN", "^1(?:3[0-9]|4[01456789]|5[0-9]|66|7[0-9]|8[0-9]|9[0-9])\\d{8}$");
        REGION_PATTERNS.put("US", "^(\\+?1)?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$");
        REGION_PATTERNS.put("JP", "^0[1-9]\\d{8,9}$");
        REGION_PATTERNS.put("KR", "^01[016789][-.\\s]?\\d{3,4}[-.\\s]?\\d{4}$");
        REGION_PATTERNS.put("UK", "^0[1-9]\\d{8,9}$");
    }

    @Override
    public void initialize(CPhone constraintAnnotation) {
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            pattern = regexp;
            return;
        }

        String region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
        pattern = REGION_PATTERNS.get(region);
        if (pattern == null) {
            throw new ConstraintDeclarationException("Unsupported @CPhone region: " + constraintAnnotation.region());
        }
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(charSequence, context);
        if (0 != vn) {
            return vn == 1;
        }

        return Pattern.matches(pattern, charSequence);
    }
}
