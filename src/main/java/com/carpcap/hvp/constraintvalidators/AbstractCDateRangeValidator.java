package com.carpcap.hvp.constraintvalidators;

import cn.hutool.core.date.DateUtil;
import com.carpcap.hvp.annotation.CDateRange;

import jakarta.validation.ConstraintValidator;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    private static final String[] DEFAULT_DATE_FORMATS = {
            "yyyyMMddHHmmssSSS",
            "yyyyMMddHHmmss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ssX",
            "yyyy-MM-dd'T'HH:mm:ss",
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd HH:mm:ss.SSS",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy年MM月dd日 HH时mm分ss秒",
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyy年MM月dd日",
            "yyyyMMdd"
    };

    public Date stringToDate(String str, String format) {
        try {
            Date result = null;
            if (str != null && !str.trim().isEmpty()) {
                if (format != null && !format.trim().isEmpty()) {
                    //规则不为空 则使用规则来进行解析
                    result = dateParse(str, format);
                } else {
                    //如果为空 则使用自动解析
                    result = dateParse(str, null);
                }
            }

//            System.out.println("hvp dateRange stringToDate：str[" + str + "], format[" + format + "] , result[" + result + "]");
            return result;
        } catch (ParseException e) {
            throw new RuntimeException("String to Date format error", e);
        }

    }

    /**
     * 使用指定格式或内置格式解析日期。
     */
    public Date dateParse(String value, String format) throws ParseException {
        //指定格式处理
        if (format != null && !format.trim().isEmpty()) {
            Date date = parseExact(value, format, Locale.getDefault());
            if (date != null) {
                return date;
            }
            throw new ParseException("Unparseable date: " + value, 0);
        }

        //无指定格式 遍历处理
        for (String defaultFormat : DEFAULT_DATE_FORMATS) {
            //EEE开头 美国地区
            Locale locale = defaultFormat.startsWith("EEE") ? Locale.ENGLISH : Locale.getDefault();
            Date date = parseExact(value, defaultFormat, locale);
            //处理成功
            if (date != null) {
                return date;
            }
        }
        throw new ParseException("Unparseable date: " + value, 0);
    }

    private Date parseExact(String value, String format, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
        dateFormat.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date date = dateFormat.parse(value, position);
        return date != null && position.getIndex() == value.length() ? date : null;
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
