package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CMoney;
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
        if (value == null) {
            return true;
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
            // 没有小数部分
            return allowNoDecimal;
        } else if (decimalPart.length() > decimalPlaces) {
            // 小数位数超过限制
            return false;
        }

        // 验证是否有前导零（不包括0本身）
        if (!allowLeadingZero && integerPart.length() > 1 && integerPart.startsWith("0")) {
            return false;
        }

        // 验证字符串格式（如果是从字符串转换的）
        if (value instanceof String) {
            return validateStringFormat(valueStr, integerPart, decimalPart);
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
     */
    private boolean validateStringFormat(String valueStr, String integerPart, String decimalPart) {

        StringBuilder regexBuilder = new StringBuilder();

        // 可选正负号
        regexBuilder.append("^[-+]?");

        // 可选货币符号
        if (allowCurrencySymbol) {
            if (allowedCurrencySymbols.isEmpty()) {
                // 正确写法：使用 OR（JDK8+）
                regexBuilder.append("(?:\\p{Sc}|\\p{Punct})?");
            } else {
                regexBuilder.append("(?:");
                for (String symbol : allowedCurrencySymbols) {
                    regexBuilder.append(Pattern.quote(symbol)).append("|");
                }
                regexBuilder.deleteCharAt(regexBuilder.length() - 1); // 删除尾部 |
                regexBuilder.append(")?");
            }
        }

        // 整数部分（带千分位时整体要放一起）
        if (allowThousandSeparator) {
            // 规则：1~3 位开头，后面可以跟多个千分位
            if (allowLeadingZero) {
                regexBuilder.append("\\d{1,3}(?:[ ,]\\d{3})*");
            } else {
                regexBuilder.append("(?:0|[1-9]\\d{0,2})(?:[ ,]\\d{3})*");
            }
        } else {
            // 不带千分位
            if (allowLeadingZero) {
                regexBuilder.append("\\d+");
            } else {
                regexBuilder.append("(?:0|[1-9]\\d*)");
            }
        }

        // 小数部分
        if (allowNoDecimal) {
            // allowNoDecimal = true → 小数可有可无
            regexBuilder.append("(?:\\.\\d{1,").append(decimalPlaces).append("})?");
        } else {
            // 必须有小数部分
            regexBuilder.append("\\.\\d{1,").append(decimalPlaces).append("}");
        }

        regexBuilder.append("$");

        Pattern pattern = Pattern.compile(regexBuilder.toString());
        Matcher matcher = pattern.matcher(valueStr);

        return matcher.matches();
    }

}