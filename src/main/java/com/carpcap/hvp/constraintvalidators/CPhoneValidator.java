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
        // 中国大陆手机号
        REGION_PATTERNS.put("CN", "^1\\d{10}$");

        // 美国手机号（数据库存10位本地号码）
        REGION_PATTERNS.put("US", "^[2-9]\\d{2}[2-9]\\d{2}\\d{4}$");

        // 日本手机号
        REGION_PATTERNS.put("JP", "^0[789]0\\d{8}$");

        // 韩国手机号
        REGION_PATTERNS.put("KR", "^01(?:0|1|6|7|8|9)\\d{8}$");

        // 英国手机号
        REGION_PATTERNS.put("UK", "^07\\d{9}$");
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
