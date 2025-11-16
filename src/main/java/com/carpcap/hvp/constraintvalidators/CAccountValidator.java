package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CAccount;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CAccountValidator extends AbstractCPatternValidator<CAccount> {

}


