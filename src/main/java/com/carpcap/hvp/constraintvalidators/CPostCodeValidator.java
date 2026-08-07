package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPostCode;
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
 * CPostCode 校验器
 * <p>支持 CN/US/JP/UK/KR 五国邮编格式，也支持自定义正则</p>
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPostCodeValidator implements ConstraintValidator<CPostCode, CharSequence> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();
    private String pattern;
    private String region;

    static {
        REGION_PATTERNS.put("CN", "^\\d{6}$");
        REGION_PATTERNS.put("US", "^\\d{5}(-\\d{4})?$");
        REGION_PATTERNS.put("JP", "^\\d{3}-\\d{4}$");
        REGION_PATTERNS.put("UK", "^(?i:GIR\\s?0AA|[A-Z]{1,2}\\d{1,2}[A-Z]?\\s?\\d[A-Z]{2})$");
        REGION_PATTERNS.put("KR", "^\\d{5}$");
    }

    @Override
    public void initialize(CPostCode constraintAnnotation) {
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            pattern = regexp;
            region = null;
            return;
        }

        region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
        pattern = REGION_PATTERNS.get(region);
        if (pattern == null) {
            throw new ConstraintDeclarationException("Unsupported @CPostCode region: " + constraintAnnotation.region());
        }
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(charSequence, context);
        if (0 != vn) {
            return vn == 1;
        }

        String value = charSequence.toString().trim();
        if (!Pattern.matches(pattern, value)) {
            return false;
        }

        if ("CN".equals(region)) {
            return !"000000".equals(value);
        }
        if ("US".equals(region)) {
            return !value.startsWith("00000");
        }
        if ("JP".equals(region)) {
            return !"000-0000".equals(value);
        }
        if ("KR".equals(region)) {
            int code = Integer.parseInt(value);
            return code >= 1000 && code <= 63644;
        }
        return true;
    }
}
