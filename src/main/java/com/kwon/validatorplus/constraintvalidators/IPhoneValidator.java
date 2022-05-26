package com.kwon.validatorplus.constraintvalidators;


import com.google.auto.service.AutoService;
import com.kwon.validatorplus.annotation.IIpAddress;
import com.kwon.validatorplus.annotation.IPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kwon
 */
@AutoService(ConstraintValidator.class)
public class IPhoneValidator  extends AbstractIPatternValidator<IPhone>  {


}


