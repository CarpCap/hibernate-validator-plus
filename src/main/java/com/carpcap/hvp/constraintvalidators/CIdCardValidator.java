package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CIdCard;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintDeclarationException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 身份号码验证器
 * 支持 CN、US、JP、KR、UK 的常用个人身份号码格式
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIdCardValidator implements ConstraintValidator<CIdCard, CharSequence> {

    private static final String REGION_CN = "CN";
    private static final Map<String, Pattern> REGION_PATTERNS = new HashMap<>();
    private static final Set<String> PROVINCE_CODES = new HashSet<>();
    private static final int[] CHECK_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final String CHECK_CHARS = "10X98765432";
    private static final int[] JP_CHECK_WEIGHTS = {6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] KR_CHECK_WEIGHTS = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};

    private boolean allowNull;
    private String region;
    private Pattern pattern;

    static {
        REGION_PATTERNS.put(REGION_CN, Pattern.compile("^\\d{17}[0-9Xx]$"));
        REGION_PATTERNS.put("US", Pattern.compile("^(?:\\d{9}|\\d{3}-\\d{2}-\\d{4})$"));
        REGION_PATTERNS.put("JP", Pattern.compile("^\\d{12}$"));
        REGION_PATTERNS.put("KR", Pattern.compile("^\\d{6}-?[1-4]\\d{6}$"));
        REGION_PATTERNS.put("UK", Pattern.compile("^(?!BG|GB|KN|NK|NT|TN|ZZ)[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z]\\d{6}[A-D]$"));

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
        String regexp = constraintAnnotation.regexp().trim();
        if (!regexp.isEmpty()) {
            this.region = null;
            this.pattern = Pattern.compile(regexp);
            return;
        }

        this.region = constraintAnnotation.region().trim().toUpperCase(Locale.ROOT);
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

        String idCard = value.toString().trim();
        if (!pattern.matcher(idCard).matches()) {
            return false;
        }

        if (region == null) {
            return true;
        }
        if (REGION_CN.equals(region)) {
            return validateCnIdCard(idCard);
        }
        if ("US".equals(region)) {
            return validateUsSsn(idCard.replace("-", ""));
        }
        if ("JP".equals(region)) {
            return validateJpMyNumber(idCard);
        }
        if ("KR".equals(region)) {
            return validateKrResidentNumber(idCard.replace("-", ""));
        }
        return true;
    }

    private boolean validateUsSsn(String idNumber) {
        String area = idNumber.substring(0, 3);
        String group = idNumber.substring(3, 5);
        String serial = idNumber.substring(5);
        return !"000".equals(area)
            && !"666".equals(area)
            && area.charAt(0) != '9'
            && !"00".equals(group)
            && !"0000".equals(serial);
    }

    private boolean validateCnIdCard(String idCard) {
        String provinceCode = idCard.substring(0, 2);
        if (!PROVINCE_CODES.contains(provinceCode)) {
            return false;
        }

        String birthDate = idCard.substring(6, 14);
        if (!isValidDate(birthDate)) {
            return false;
        }
        return validateCnCheckDigit(idCard);
    }

    private boolean validateCnCheckDigit(String idCard) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard.charAt(i) - '0') * CHECK_WEIGHTS[i];
        }
        char expectedCheckChar = CHECK_CHARS.charAt(sum % 11);
        char actualCheckChar = Character.toUpperCase(idCard.charAt(17));
        return actualCheckChar == expectedCheckChar;
    }

    private boolean validateJpMyNumber(String idNumber) {
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            sum += (idNumber.charAt(i) - '0') * JP_CHECK_WEIGHTS[i];
        }
        int remainder = sum % 11;
        int expected = remainder <= 1 ? 0 : 11 - remainder;
        return idNumber.charAt(11) - '0' == expected;
    }

    private boolean validateKrResidentNumber(String idNumber) {
        int centuryCode = idNumber.charAt(6) - '0';
        String century = centuryCode == 1 || centuryCode == 2 ? "19" : "20";
        if (!isValidDate(century + idNumber.substring(0, 6))) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (idNumber.charAt(i) - '0') * KR_CHECK_WEIGHTS[i];
        }
        int expected = (11 - sum % 11) % 10;
        return idNumber.charAt(12) - '0' == expected;
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
