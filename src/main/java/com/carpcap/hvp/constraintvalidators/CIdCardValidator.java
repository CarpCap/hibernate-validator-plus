package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CIdCard;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 身份证号验证器
 * 支持15位和18位身份证，包含省份代码校验、校验位算法（ISO 7064:1983, MOD 11-2）
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIdCardValidator implements ConstraintValidator<CIdCard, CharSequence> {

    private boolean allowNull;

    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{15}$|^\\d{18}$");
    private static final Set<String> PROVINCE_CODES = new HashSet<>();
    private static final int[] CHECK_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final String CHECK_CHARS = "10X98765432";

    static {
        // 省级行政区划代码（前两位）
        String[] codes = {
            "11", "12", "13", "14", "15",    // 京津冀晋蒙
            "21", "22", "23",                // 辽吉黑
            "31", "32", "33", "34", "35", "36", "37", // 沪苏浙皖闽赣鲁
            "41", "42", "43", "44", "45", "46",       // 豫鄂湘粤桂琼
            "50", "51", "52", "53", "54",             // 渝川贵滇藏
            "61", "62", "63", "64", "65",             // 陕甘青宁新
            "71", "81", "82"                          // 港澳台
        };
        for (String code : codes) {
            PROVINCE_CODES.add(code);
        }
    }

    @Override
    public void initialize(CIdCard constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.toString().trim().isEmpty()) {
            return allowNull;
        }

        String idCard = value.toString().trim();

        // 1) 基础格式校验：15位或18位纯数字
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }

        // 2) 省份代码校验
        String provinceCode = idCard.substring(0, 2);
        if (!PROVINCE_CODES.contains(provinceCode)) {
            return false;
        }

        // 3) 18位身份证校验位校验（ISO 7064:1983, MOD 11-2）
        if (idCard.length() == 18) {
            return validateCheckDigit(idCard);
        }

        // 15位身份证：无校验位，仅做基本校验
        return true;
    }

    /**
     * 验证18位身份证的校验位
     */
    private boolean validateCheckDigit(String idCard) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = idCard.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * CHECK_WEIGHTS[i];
        }

        int mod = sum % 11;
        char expectedCheckChar = CHECK_CHARS.charAt(mod);
        char actualCheckChar = Character.toUpperCase(idCard.charAt(17));

        return actualCheckChar == expectedCheckChar;
    }
}
