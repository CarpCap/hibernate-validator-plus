package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CMacAddress;
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
        if (value == null || value.toString().trim().isEmpty()) {
            return true;
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
        String hexBytePattern;

        if (allowOmittingLeadingZero) {
            hexBytePattern = "(" + hexCharPattern + "(?:" + hexCharPattern + ")?)";
        } else {
            hexBytePattern = "(" + hexCharPattern + "{2})";
        }

        // 支持的分隔符：冒号、连字符或无分隔符
        String separatorPattern = "([:-])?";
        
        // 基本的MAC地址格式（6字节）
        String mac48Pattern = "^" + hexBytePattern + separatorPattern + 
                               hexBytePattern + separatorPattern + 
                               hexBytePattern + separatorPattern + 
                               hexBytePattern + separatorPattern + 
                               hexBytePattern + separatorPattern + 
                               hexBytePattern + "$";

        if (allowEui64) {
            // EUI-64格式（8字节）
            String eui64Pattern = "^" + hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + separatorPattern + 
                                  hexBytePattern + "$";
            
            // 组合两种模式
            patternBuilder.append("(");
            patternBuilder.append(mac48Pattern.substring(1, mac48Pattern.length() - 1));
            patternBuilder.append("|").append(eui64Pattern.substring(1, eui64Pattern.length() - 1));
            patternBuilder.append(")");
        } else {
            patternBuilder.append(mac48Pattern);
        }

        return patternBuilder.toString();
    }
}