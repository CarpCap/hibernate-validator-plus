package com.carpcap.hvp.constraintvalidators;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

/**
 * LocalDate Type Date Range Validator
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CDateRangeLocalDateTimeValidator extends AbstractCDateRangeValidator<LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }

        ConstraintValidatorContextImpl cvc = (ConstraintValidatorContextImpl) context;
        Map<String, Object> attributes = cvc.getConstraintDescriptor().getAttributes();

        String max = attributes.get("max").toString();
        String min = attributes.get("min").toString();
        String format = attributes.get("format").toString();

        Date date = Date.from(value
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return super.isValid(date, max, min, format);
    }


}
