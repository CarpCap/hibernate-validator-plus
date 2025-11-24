package com.carpcap.hvp.constraintvalidators;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import com.carpcap.hvp.annotation.CDateRange;

import javax.validation.ConstraintValidator;
import java.util.Date;

/**
 * 抽象类 日期验证器
 *
 * @author CarpCap
 */
public abstract class AbstractCDateRangeValidator<T> implements ConstraintValidator<CDateRange, T> {


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


}
