package com.carpcap.validatorplus.constraintvalidators;


import com.carpcap.validatorplus.annotation.IDomain;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class IDomainValidator extends AbstractIPatternValidator<IDomain> {


}


