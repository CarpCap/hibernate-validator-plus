package com.carpcap.hvp.constraintvalidators;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import com.carpcap.hvp.annotation.CDateRange;

import javax.validation.ConstraintValidator;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 抽象类 日期验证器
 *
 * @author CarpCap
 */
public abstract class AbstractCDateRangeValidator<T> implements ConstraintValidator<CDateRange, T> {

    private static final Pattern AUTO_TIME_PATTERN = Pattern.compile(
            "(?i)(?:\\d{1,2}:\\d{2}|T\\d{1,2}|\\b(?:AM|PM)\\b|[时分秒])");
    private static final Pattern COMPACT_DATE_TIME_PATTERN = Pattern.compile("^\\d{9,17}$");

    public Date stringToDate(String str, String format) {
        try {
            Date result = null;
            if (str != null && !str.trim().isEmpty()) {
                if (format != null && !format.trim().isEmpty()) {
                    //规则不为空 则使用规则来进行解析
                    result = DateUtil.parse(str, format);
                } else {
                    //如果为空 则使用自动解析
                    result = DateUtil.parse(str);
                }
            }

//            System.out.println("hvp dateRange stringToDate：str[" + str + "], format[" + format + "] , result[" + result + "]");
            return result;
        } catch (DateException e) {
            e.printStackTrace();
            throw new RuntimeException("String to Date format error ");
        }

    }

    public boolean isValid(Date value, String max, String min, String format) {
        if (value == null) {
            return true;
        }

        Date maxDate = stringToDate(max, format);
        if (maxDate != null && !hasTimePrecision(max, format)) {
            maxDate = DateUtil.endOfDay(maxDate);
        }
        Date minDate = stringToDate(min, format);


//        System.out.println("hvp dateRange compare：valueDate[" + value + "] minDate[" + minDate + "] maxDate[" + maxDate + "]");
        if (maxDate != null && value.compareTo(maxDate) > 0) {
            return false;
        }
        if (minDate != null && value.compareTo(minDate) < 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断上限配置是否明确包含时间，避免把精确时间扩展到当天结束。
     */
    private boolean hasTimePrecision(String value, String format) {
        if (format != null && !format.trim().isEmpty()) {
            return formatContainsTime(format);
        }

        String text = value == null ? "" : value.trim();
        return AUTO_TIME_PATTERN.matcher(text).find()
                || COMPACT_DATE_TIME_PATTERN.matcher(text).matches();
    }

    private boolean formatContainsTime(String format) {
        boolean quoted = false;
        for (int i = 0; i < format.length(); i++) {
            char current = format.charAt(i);
            if (current == '\'') {
                if (i + 1 < format.length() && format.charAt(i + 1) == '\'') {
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (!quoted && "HhKkmsSa".indexOf(current) >= 0) {
                return true;
            }
        }
        return false;
    }

}
