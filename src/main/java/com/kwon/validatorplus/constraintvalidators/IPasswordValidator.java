package com.kwon.validatorplus.constraintvalidators;


import com.google.auto.service.AutoService;
import com.kwon.validatorplus.annotation.IIdCard;
import com.kwon.validatorplus.annotation.IPassword;

import javax.validation.ConstraintValidator;

/**
 *
 * @author Kwon
 */
@AutoService(ConstraintValidator.class)
public class IPasswordValidator extends AbstractIPatternValidator<IPassword>  {


}


