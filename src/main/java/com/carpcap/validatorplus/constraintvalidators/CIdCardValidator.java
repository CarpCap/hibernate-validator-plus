package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.CIdCard;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIdCardValidator extends AbstractCPatternValidator<CIdCard> {


}


