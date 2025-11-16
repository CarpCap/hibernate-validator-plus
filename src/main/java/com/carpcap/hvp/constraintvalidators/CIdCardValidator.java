package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CIdCard;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CIdCardValidator extends AbstractCPatternValidator<CIdCard> {


}


