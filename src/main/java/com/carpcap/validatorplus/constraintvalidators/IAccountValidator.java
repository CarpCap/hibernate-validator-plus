package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.IAccount;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class IAccountValidator extends AbstractIPatternValidator<IAccount> {

}


