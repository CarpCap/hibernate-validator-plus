package com.kwon.validatorplus.constraintvalidators;


import com.google.auto.service.AutoService;
import com.kwon.validatorplus.annotation.IAccount;

import javax.validation.ConstraintValidator;

/**
 *
 * @author Kwon
 */
@AutoService(ConstraintValidator.class)
public class IAccountValidator extends AbstractIPatternValidator<IAccount> {

}


