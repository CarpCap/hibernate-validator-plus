package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CMacAddress;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * MAC地址验证器
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CMacAddressValidator implements ConstraintValidator<CMacAddress, CharSequence> {

    private boolean allowLowercase;
    private boolean allowOmittingLeadingZero;
    private boolean allowEui64;

    @Override
    public void initialize(CMacAddress constraintAnnotation) {
        this.allowLowercase = constraintAnnotation.allowLowercase();
        this.allowOmittingLeadingZero = constraintAnnotation.allowOmittingLeadingZero();
        this.allowEui64 = constraintAnnotation.allowEui64();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }


        String macAddress = value.toString().trim();

        // 根据配置构建不同的正则表达式
        String patternStr = buildMacAddressPattern();
        return Pattern.matches(patternStr, macAddress);
    }

    /**
     * 根据配置构建MAC地址验证的正则表达式
     */
    private String buildMacAddressPattern() {
        StringBuilder patternBuilder = new StringBuilder();
        String hexCharPattern = allowLowercase ? "[0-9A-Fa-f]" : "[0-9A-F]";
        String hexByte;

        if (allowOmittingLeadingZero) {
            hexByte = "(" + hexCharPattern + "(?:" + hexCharPattern + ")?)";
        } else {
            hexByte = "(" + hexCharPattern + "{2})";
        }

        // 三种分离模式，各自强制分隔符一致
        // 1) 无分隔符: AABBCCDDEEFF
        // 2) 冒号分隔: AA:BB:CC:DD:EE:FF
        // 3) 连字符分隔: AA-BB-CC-DD-EE-FF
        int count = allowEui64 ? 8 : 6;

        String noSep = hexByte;
        String colonSep = hexByte;
        String hyphenSep = hexByte;
        for (int i = 1; i < count; i++) {
            noSep += hexByte;
            colonSep += ":" + hexByte;
            hyphenSep += "-" + hexByte;
        }

        patternBuilder.append("^(");
        patternBuilder.append(noSep);
        patternBuilder.append("|");
        patternBuilder.append(colonSep);
        patternBuilder.append("|");
        patternBuilder.append(hyphenSep);
        patternBuilder.append(")$");

        return patternBuilder.toString();
    }
}
