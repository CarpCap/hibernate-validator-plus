package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.CIpv4;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIpAddressValidator extends AbstractCPatternValidator<CIpv4> {


}


