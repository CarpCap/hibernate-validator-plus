package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.IIpAddress;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class IIpAddressValidator extends AbstractIPatternValidator<IIpAddress> {


}


