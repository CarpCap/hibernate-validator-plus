package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CDomain;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidatorContext;
import java.net.IDN;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CDomainValidator implements ConstraintValidator<CDomain, CharSequence> {
    private static final String DEFAULT_MESSAGE = "{com.carpcap.hvp.annotation.CDomain.message}";
    private static final String LEVEL_MESSAGE = "{com.carpcap.hvp.annotation.CDomain.level.message}";
    private static final String TLD_MESSAGE = "{com.carpcap.hvp.annotation.CDomain.tld.message}";
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^(?!\\d+(?:\\.\\d+)+$)[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$");
    private int level;
    private boolean useDefaultMessage;
    private boolean allowTld;

    @Override
    public void initialize(CDomain annotation) {
        level = annotation.level();
        allowTld = annotation.allowTld();
        useDefaultMessage = DEFAULT_MESSAGE.equals(annotation.message());
        if (level < -1) {
            throw new ConstraintDeclarationException("CDomain.level 不能小于 -1");
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int nullResult = CValidNullUtil.validNull(value, context);
        if (nullResult != 0) return nullResult == 1;
        String domain;
        try {
            domain = IDN.toASCII(value.toString(), IDN.USE_STD3_ASCII_RULES).toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        if (domain.length() > 253 || domain.startsWith(".") || domain.endsWith(".")) return false;
        String[] labels = domain.split("\\.", -1);
        if (!allowTld && labels.length == 1) return invalid(context, TLD_MESSAGE);
        if (level >= 0 && labels.length > level + 1) return invalid(context);
        return DOMAIN_PATTERN.matcher(domain).matches();
    }

    private boolean invalid(ConstraintValidatorContext context) {
        return invalid(context, LEVEL_MESSAGE);
    }

    private boolean invalid(ConstraintValidatorContext context, String message) {
        if (useDefaultMessage) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return false;
    }

}


