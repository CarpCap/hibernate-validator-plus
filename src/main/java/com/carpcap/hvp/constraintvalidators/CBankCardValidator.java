package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CBankCard;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 银行卡号验证器
 * 使用Luhn算法验证银行卡号的有效性
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CBankCardValidator implements ConstraintValidator<CBankCard, CharSequence> {

    private int minLength;
    private int maxLength;
    //Luhn Check
    private boolean useLuhnCheck;
    //允许分隔符
    private boolean allowSpaces;
    //允许 连字分隔符
    private boolean allowHyphens;
    //白名单前缀
    private Set<String> allowedPrefixes;
    //黑名单前缀
    private Set<String> forbiddenPrefixes;

    @Override
    public void initialize(CBankCard constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.useLuhnCheck = constraintAnnotation.useLuhnCheck();
        this.allowSpaces = constraintAnnotation.allowSpaces();
        this.allowHyphens = constraintAnnotation.allowHyphens();

        // 初始化允许的前缀集合
        String[] allowed = constraintAnnotation.allowedPrefixes();
        if (allowed != null && allowed.length > 0) {
            this.allowedPrefixes = new HashSet<>(Arrays.asList(allowed));
        } else {
            this.allowedPrefixes = new HashSet<>();
        }
        
        // 初始化禁止的前缀集合
        String[] forbidden = constraintAnnotation.forbiddenPrefixes();
        if (forbidden != null && forbidden.length > 0) {
            this.forbiddenPrefixes = new HashSet<>(Arrays.asList(forbidden));
        } else {
            this.forbiddenPrefixes = new HashSet<>();
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }

        String cardNumber = value.toString().trim();
        
        // 清理卡号（移除分隔符）
        String cleanedNumber = cleanCardNumber(cardNumber);
        
        // 验证是否全为数字
        if (!cleanedNumber.matches("\\d+")) {
            return false;
        }
        
        // 验证卡号长度
        if (cleanedNumber.length() < minLength || cleanedNumber.length() > maxLength) {
            return false;
        }
        
        // 验证前缀
        if (!validatePrefix(cleanedNumber)) {
            return false;
        }
        
        // 使用Luhn算法验证
        if (useLuhnCheck && !luhnCheck(cleanedNumber)) {
            return false;
        }
        
        return true;
    }

    /**
     * 清理银行卡号，移除分隔符
     */
    private String cleanCardNumber(String cardNumber) {
        StringBuilder cleaned = new StringBuilder();
        
        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            if (Character.isDigit(c)) {
                cleaned.append(c);
            } else if ((allowSpaces && c == ' ') || (allowHyphens && c == '-')) {
                // 跳过允许的分隔符
                continue;
            } else {
                // 不允许的字符
                return cardNumber; // 返回原始字符串，让后续验证失败
            }
        }
        
        return cleaned.toString();
    }

    /**
     * 验证卡号前缀
     */
    private boolean validatePrefix(String cardNumber) {
        // 检查禁止的前缀
        for (String prefix : forbiddenPrefixes) {
            if (cardNumber.startsWith(prefix)) {
                return false;
            }
        }
        
        // 检查允许的前缀（如果有指定）
        if (!allowedPrefixes.isEmpty()) {
            for (String prefix : allowedPrefixes) {
                if (cardNumber.startsWith(prefix)) {
                    return true;
                }
            }
            return false; // 如果指定了允许的前缀，但卡号不匹配任何一个
        }
        
        return true; // 没有指定前缀限制或没有禁止的前缀
    }

    /**
     * 使用Luhn算法验证银行卡号
     * Luhn算法步骤：
     * 1. 从卡号的最后一位数字开始，向前计算
     * 2. 对于奇数位置的数字，直接相加
     * 3. 对于偶数位置的数字，先乘以2，如果结果大于9，则减去9，然后相加
     * 4. 将所有数字相加，结果必须能被10整除
     */
    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        // 从右到左遍历
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}