package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CMoney;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额验证器
 * 支持验证数字、字符串或BigDecimal类型的金额格式
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CMoneyValidator implements ConstraintValidator<CMoney, Object> {

    //最低金额 包含
    private double min;
    //最高金额 包含
    private double max;
    // 最小整数位数（包含），默认为1
    private int minIntegerDigits;
    // 最大金额值（包含），默认为Double.MAX_VALUE
    private int maxIntegerDigits;
    private int decimalPlaces;
    private boolean allowNoDecimal;
    private boolean allowLeadingZero;
    private boolean allowCurrencySymbol;
    private Set<String> allowedCurrencySymbols;
    private boolean allowThousandSeparator;

    @Override
    public void initialize(CMoney constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.minIntegerDigits = constraintAnnotation.minIntegerDigits();
        this.maxIntegerDigits = constraintAnnotation.maxIntegerDigits();
        this.decimalPlaces = constraintAnnotation.decimalPlaces();
        this.allowNoDecimal = constraintAnnotation.allowNoDecimal();
        this.allowLeadingZero = constraintAnnotation.allowLeadingZero();
        this.allowCurrencySymbol = constraintAnnotation.allowCurrencySymbol();
        this.allowThousandSeparator = constraintAnnotation.allowThousandSeparator();
        
        // 初始化允许的货币符号集合
        String[] symbols = constraintAnnotation.allowedCurrencySymbols();
        if (symbols != null && symbols.length > 0) {
            this.allowedCurrencySymbols = new HashSet<>(Arrays.asList(symbols));
        } else {
            this.allowedCurrencySymbols = new HashSet<>();
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }


        BigDecimal amount;
        String valueStr;

        // 处理不同类型的输入
        if (value instanceof Number) {
            // 数字类型
            amount = new BigDecimal(value.toString()).stripTrailingZeros();
            valueStr = value.toString();
        } else if (value instanceof String) {
            // 字符串类型，需要先处理格式
            valueStr = ((String) value).trim();
            if (valueStr.isEmpty()) {
                return true;
            }
            
            try {
                // 清理格式化的金额字符串
                String cleanedStr = cleanMoneyString(valueStr);
                amount = new BigDecimal(cleanedStr).stripTrailingZeros();
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            // 不支持的类型
            return false;
        }

        // 验证金额范围
        if (amount.compareTo(BigDecimal.valueOf(min)) < 0 || 
            amount.compareTo(BigDecimal.valueOf(max)) > 0) {
            return false;
        }

        // 验证整数部分位数
        String integerPart = amount.abs().toBigInteger().toString();
        if (integerPart.length() < minIntegerDigits || 
            integerPart.length() > maxIntegerDigits) {
            return false;
        }

        // 验证小数部分位数
        String decimalPart = getDecimalPart(amount);
        if (decimalPart == null) {
            // Number 类型无小数概念，跳过表示层检验
            if (!(value instanceof Number)) {
                return allowNoDecimal;
            }
        } else if (decimalPart.length() > decimalPlaces) {
            // 小数位数超过限制
            return false;
        }

        // 验证是否有前导零（不包括0本身）
        if (!allowLeadingZero && integerPart.length() > 1 && integerPart.startsWith("0")) {
            return false;
        }

        // 验证字符串输入 —— 先校验原始格式（货币符号、千分位），再校验清理后格式
        if (value instanceof String) {
            if (!validateRawFormat(valueStr)) {
                return false;
            }
            return validateStringFormat(cleanMoneyString(valueStr), integerPart, decimalPart);
        }

        return true;
    }

    /**
     * 清理格式化的金额字符串
     */
    private String cleanMoneyString(String valueStr) {
        StringBuilder cleaned = new StringBuilder();
        
        // 处理货币符号
        if (allowCurrencySymbol && !valueStr.isEmpty()) {
            char firstChar = valueStr.charAt(0);
            if (!Character.isDigit(firstChar) && firstChar != '-' && firstChar != '+') {
                // 检查是否是允许的货币符号
                String symbol = String.valueOf(firstChar);
                if (allowedCurrencySymbols.isEmpty() || allowedCurrencySymbols.contains(symbol)) {
                    // 去掉货币符号
                    valueStr = valueStr.substring(1).trim();
                }
            }
        }
        
        // 移除千分位分隔符
        if (allowThousandSeparator) {
            valueStr = valueStr.replace(",", "").replace(" ", "");
        }
        
        return valueStr;
    }

    /**
     * 获取BigDecimal的小数部分
     */
    private String getDecimalPart(BigDecimal amount) {
        if (amount.scale() <= 0) {
            return null;
        }
        
        String amountStr = amount.toPlainString();
        int dotIndex = amountStr.indexOf('.');
        if (dotIndex == -1) {
            return null;
        }
        
        return amountStr.substring(dotIndex + 1);
    }

    /**
     * 验证金额字符串的格式
     * 对清理后的纯数字字符串进行格式验证
     */
    private boolean validateStringFormat(String cleanedValueStr, String integerPart, String decimalPart) {

        StringBuilder regexBuilder = new StringBuilder();

        // 可选正负号
        regexBuilder.append("^[-+]?");

        // 整数部分 — 清理后字符串已是纯数字格式
        if (allowLeadingZero) {
            regexBuilder.append("\\d+");
        } else {
            regexBuilder.append("(?:0|[1-9]\\d*)");
        }

        // 小数部分
        if (allowNoDecimal) {
            regexBuilder.append("(?:\\.\\d{1,").append(decimalPlaces).append("})?");
        } else {
            regexBuilder.append("\\.\\d{1,").append(decimalPlaces).append("}");
        }

        regexBuilder.append("$");

        Pattern pattern = Pattern.compile(regexBuilder.toString());
        Matcher matcher = pattern.matcher(cleanedValueStr);

        return matcher.matches();
    }

    /**
     * 验证原始输入字符串的装饰格式（货币符号、千分位）
     * 与 cleanMoneyString() 使用同一套规则，确保格式校验和数值校验同步
     */
    private boolean validateRawFormat(String valueStr) {
        String raw = valueStr;

        // 1) 验证货币符号
        if (allowCurrencySymbol) {
            char firstChar = raw.charAt(0);
            if (!Character.isDigit(firstChar) && firstChar != '-' && firstChar != '+') {
                // 有货币符号前缀 —— 验证是否在允许列表中
                String symbol = String.valueOf(firstChar);
                if (!allowedCurrencySymbols.isEmpty() && !allowedCurrencySymbols.contains(symbol)) {
                    return false;
                }
                raw = raw.substring(1).trim();
            }
            // 无货币符号也允许（allowCurrencySymbol 表示"允许出现"，而非"必须出现"）
        } else {
            // 不允许货币符号时，首位不应是货币符号类字符
            char first = raw.charAt(0);
            if (!Character.isDigit(first) && first != '-' && first != '+') {
                return false;
            }
        }

        // 2) 验证千分位
        if (allowThousandSeparator) {
            int dotIndex = raw.indexOf('.');
            String intPart = (dotIndex == -1) ? raw : raw.substring(0, dotIndex);
            if (intPart.startsWith("-") || intPart.startsWith("+")) {
                intPart = intPart.substring(1);
            }
            if (intPart.contains(",")) {
                if (!intPart.matches("\\d{1,3}(,\\d{3})*")) {
                    return false;
                }
            }
        } else {
            if (raw.contains(",") || raw.contains(" ")) {
                return false;
            }
        }

        // 3) 清理后验证是否还有意外字符
        StringBuilder cleanedCheck = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == '-' || c == '+') {
                cleanedCheck.append(c);
            } else if (c == ',') {
                // 千分位已在步骤2验证，跳过
            } else {
                return false;
            }
        }

        try {
            new BigDecimal(cleanedCheck.toString());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}
