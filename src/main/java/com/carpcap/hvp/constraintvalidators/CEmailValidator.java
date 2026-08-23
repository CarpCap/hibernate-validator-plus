package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CEmail;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.IDN;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 邮箱格式与域名策略验证器。
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CEmailValidator implements ConstraintValidator<CEmail, CharSequence> {

    private static final String DEFAULT_MESSAGE = "{com.carpcap.hvp.annotation.CEmail.message}";
    private static final String BLACKLIST_MESSAGE = "{com.carpcap.hvp.annotation.CEmail.blacklist.message}";
    private static final String WHITELIST_MESSAGE = "{com.carpcap.hvp.annotation.CEmail.whitelist.message}";
    private static final String LEVEL_MESSAGE = "{com.carpcap.hvp.annotation.CEmail.level.message}";
    private static final String TLD_MESSAGE = "{com.carpcap.hvp.annotation.CEmail.tld.message}";
    private static final Pattern LOCAL_PART_PATTERN = Pattern.compile(
        "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*$"
    );
    private static final Pattern DOMAIN_LABEL_PATTERN = Pattern.compile("^[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?$");

    private CEmail.ListMode listMode;
    private Set<String> domains;
    private int level;
    private boolean useDefaultMessage;
    private boolean allowTld;

    @Override
    public void initialize(CEmail annotation) {
        this.listMode = annotation.listMode();
        this.level = annotation.level();
        this.allowTld = annotation.allowTld();
        this.useDefaultMessage = DEFAULT_MESSAGE.equals(annotation.message());
        if (level < -1) {
            throw new ConstraintDeclarationException("CEmail.level 不能小于 -1");
        }

        Set<String> normalizedDomains = new HashSet<>();
        for (String domain : annotation.domains()) {
            String normalized = normalizeDomain(domain);
            if (normalized == null || !isValidDomain(normalized)) {
                throw new ConstraintDeclarationException("CEmail.domains 包含无效域名: " + domain);
            }
            normalizedDomains.add(normalized);
        }
        this.domains = Collections.unmodifiableSet(normalizedDomains);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int nullResult = CValidNullUtil.validNull(value, context);
        if (nullResult != 0) {
            return nullResult == 1;
        }

        String email = value.toString().trim();
        if (email.length() > 254) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@') || atIndex == email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domain = normalizeDomain(email.substring(atIndex + 1));
        if (!isValidLocalPart(localPart) || domain == null || !isValidDomain(domain)) {
            return false;
        }
        if (!allowTld && domain.indexOf('.') < 0) {
            return invalid(context, TLD_MESSAGE);
        }
        if (localPart.length() + 1 + domain.length() > 254) {
            return false;
        }
        if (level >= 0 && domainLevelOf(domain) > level) {
            return invalid(context, LEVEL_MESSAGE);
        }

        boolean listed = isListed(domain);
        if (listMode == CEmail.ListMode.BLACKLIST && listed) {
            return invalid(context, BLACKLIST_MESSAGE);
        }
        if (listMode == CEmail.ListMode.WHITELIST && !listed) {
            return invalid(context, WHITELIST_MESSAGE);
        }
        return true;
    }

    /**
     * 默认消息按失败原因切换，自定义消息保持原样。
     */
    private boolean invalid(ConstraintValidatorContext context, String messageTemplate) {
        if (useDefaultMessage) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        }
        return false;
    }

    private static boolean isValidLocalPart(String localPart) {
        return localPart.length() <= 64 && LOCAL_PART_PATTERN.matcher(localPart).matches();
    }

    private static String normalizeDomain(String domain) {
        if (domain == null) {
            return null;
        }
        String value = domain.trim();
        if (value.startsWith("@")) {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            return null;
        }
        try {
            return IDN.toASCII(value, IDN.USE_STD3_ASCII_RULES).toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private static boolean isValidDomain(String domain) {
        if (domain.length() > 253 || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }
        String[] labels = domain.split("\\.", -1);
        for (String label : labels) {
            if (!DOMAIN_LABEL_PATTERN.matcher(label).matches()) {
                return false;
            }
        }
        return true;
    }

    private static int domainLevelOf(String domain) {
        int level = 0;
        for (int i = 0; i < domain.length(); i++) {
            if (domain.charAt(i) == '.') {
                level++;
            }
        }
        return level;
    }

    private boolean isListed(String domain) {
        for (String listedDomain : domains) {
            if (domain.equals(listedDomain) || domain.endsWith("." + listedDomain)) {
                return true;
            }
        }
        return false;
    }
}
