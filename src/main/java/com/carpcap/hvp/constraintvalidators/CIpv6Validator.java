package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CIpv6;
import com.carpcap.hvp.utils.CValidNullUtil;
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
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
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