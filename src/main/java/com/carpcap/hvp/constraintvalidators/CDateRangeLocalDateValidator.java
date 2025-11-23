package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * LocalDate Type Date Range Validator
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CDateRangeLocalDateValidator extends AbstractCDateRangeValidator<LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }

        ConstraintValidatorContextImpl cvc = (ConstraintValidatorContextImpl) context;
        Map<String, Object> attributes = cvc.getConstraintDescriptor().getAttributes();

        String max = attributes.get("max").toString();
        String min = attributes.get("min").toString();
        String format = attributes.get("format").toString();

        Date date = Date.from(
                value.atStartOfDay(ZoneId.of("UTC")).toInstant()
        );
        return super.isValid(date, max, min, format);
    }


}
