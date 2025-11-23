package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CIpv6;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIpv6Validator implements ConstraintValidator<CIpv6, CharSequence> {


    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // 空值检查由@NotNull等注解处理
        if (value == null || value.toString().trim().isEmpty()) {
            return true;
        }

        String valueStr = value.toString();


        return isIPv6(valueStr);
    }


    public static boolean isIPv6(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr instanceof Inet6Address;
        } catch (Exception e) {
            return false;
        }
    }


}