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

    static {
        // 中国
        REGION_PATTERNS.put("CN", "^\\d{6}$");

        // 美国
        REGION_PATTERNS.put("US", "^\\d{5}$");

        // 日本
        REGION_PATTERNS.put("JP", "^\\d{7}$");

        // 英国
        REGION_PATTERNS.put("UK", "^(?i:GIR0AA|[A-Z]{1,2}\\d{1,2}[A-Z]?\\d[A-Z]{2})$");

        // 韩国
        REGION_PATTERNS.put("KR", "^\\d{5}$");
    }

    @Override
    public void initialize(CPostCode constraintAnnotation) {
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            pattern = regexp;
            return;
        }

        String region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
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

        return Pattern.matches(pattern, charSequence.toString().trim());
    }
}
