package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPassport;
import com.google.auto.service.AutoService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintDeclarationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 护照号验证器
 * <p>支持多个国家/地区的护照号格式验证</p>
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPassportValidator implements ConstraintValidator<CPassport, CharSequence> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();

    static {
        // 中国护照
        REGION_PATTERNS.put("CN", "^[A-Z]\\d{8}$");

        // 美国护照
        REGION_PATTERNS.put("US", "^\\d{9}$");

        // 日本护照
        REGION_PATTERNS.put("JP", "^[A-Z]{1,2}\\d{7}$");

        // 英国护照
        REGION_PATTERNS.put("UK", "^\\d{9}$");

        // 韩国护照
        REGION_PATTERNS.put("KR", "^(?:[A-Z]\\d{8}|\\d{9})$");
    }

    private boolean allowNull;
    private String pattern;

    @Override
    public void initialize(CPassport constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            pattern = regexp;
            return;
        }

        String region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
        pattern = REGION_PATTERNS.get(region);
        if (pattern == null) {
            throw new ConstraintDeclarationException("Unsupported @CPassport region: " + constraintAnnotation.region());
        }
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        // 统一判空处理
        if (charSequence == null || charSequence.toString().trim().isEmpty()) {
            return allowNull;
        }

        return Pattern.matches(pattern, charSequence.toString().trim());
    }
}
