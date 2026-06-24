package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPassport;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
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
        // 中国护照: 1个大写字母 + 8位数字 (普通护照: E/G开头, 公务: S, 外交: D)
        REGION_PATTERNS.put("CN", "^[A-Z]\\d{8}$");
        // 美国护照: 9位数字
        REGION_PATTERNS.put("US", "^\\d{9}$");
        // 日本护照: 2个大写字母 + 7位数字
        REGION_PATTERNS.put("JP", "^[A-Z]{2}\\d{7}$");
        // 英国护照: 1个大写字母 + 8位数字
        REGION_PATTERNS.put("UK", "^[A-Z]\\d{8}$");
        // 韩国护照: 1个大写字母 + 8位数字
        REGION_PATTERNS.put("KR", "^[A-Z]\\d{8}$");
    }

    private boolean allowNull;

    @Override
    public void initialize(CPassport constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        // 统一判空处理
        if (charSequence == null || charSequence.toString().trim().isEmpty()) {
            return allowNull;
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

        return Pattern.matches(pattern, charSequence.toString().trim());
    }
}
