package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CIdCard;
import com.google.auto.service.AutoService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintDeclarationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 身份号码验证器
 * 支持 CN、US、JP、KR、UK 的身份号码格式
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIdCardValidator implements ConstraintValidator<CIdCard, CharSequence> {

    private static final Map<String, String> REGION_PATTERNS = new HashMap<>();

    private boolean allowNull;
    private String pattern;

    static {
        // 中国身份证
        REGION_PATTERNS.put("CN", "^\\d{17}[0-9Xx]$");

        // 美国 SSN
        REGION_PATTERNS.put("US", "^\\d{9}$");

        // 日本 My Number
        REGION_PATTERNS.put("JP", "^\\d{12}$");

        // 韩国居民登记号码
        REGION_PATTERNS.put("KR", "^\\d{13}$");

        // 英国 National Insurance Number
        REGION_PATTERNS.put("UK", "^[A-Z]{2}\\d{6}[A-Z]$");
    }

    @Override
    public void initialize(CIdCard constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            this.pattern = regexp;
            return;
        }

        String region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
        this.pattern = REGION_PATTERNS.get(region);
        if (pattern == null) {
            throw new ConstraintDeclarationException("Unsupported @CIdCard region: " + constraintAnnotation.region());
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.toString().trim().isEmpty()) {
            return allowNull;
        }

        return Pattern.matches(pattern, value.toString().trim());
    }
}
