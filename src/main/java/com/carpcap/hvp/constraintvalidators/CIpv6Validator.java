package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CIpv6;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIpv6Validator implements ConstraintValidator<CIpv6, CharSequence> {


    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }
        return isIPv6(value.toString());
    }


    public static boolean isIPv6(String ip) {
        if (ip == null || ip.isEmpty() || ip.length() > 39) {
            return false;
        }

        int compressionIndex = ip.indexOf("::");
        if (compressionIndex != ip.lastIndexOf("::")) {
            return false;
        }

        if (compressionIndex < 0) {
            return countGroups(ip) == 8;
        }

        int leftGroups = countGroups(ip.substring(0, compressionIndex));
        int rightGroups = countGroups(ip.substring(compressionIndex + 2));
        return leftGroups >= 0 && rightGroups >= 0 && leftGroups + rightGroups < 8;
    }

    /**
     * 统计 IPv6 十六进制分组，非法分组返回 -1。
     */
    private static int countGroups(String value) {
        if (value.isEmpty()) {
            return 0;
        }

        String[] groups = value.split(":", -1);
        for (String group : groups) {
            if (group.isEmpty() || group.length() > 4 || !isHex(group)) {
                return -1;
            }
        }
        return groups.length;
    }

    private static boolean isHex(String value) {
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (!((ch >= '0' && ch <= '9')
                    || (ch >= 'a' && ch <= 'f')
                    || (ch >= 'A' && ch <= 'F'))) {
                return false;
            }
        }
        return true;
    }

}
