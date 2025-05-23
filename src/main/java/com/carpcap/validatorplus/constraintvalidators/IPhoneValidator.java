package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.IPhone;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class IPhoneValidator  extends AbstractIPatternValidator<IPhone>  {


}


