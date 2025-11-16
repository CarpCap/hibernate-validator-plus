package com.carpcap.validatorplus.constraintvalidators;

import com.carpcap.validatorplus.annotation.CPlateNumber;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;


@AutoService(ConstraintValidator.class)
public class IPlateNumberValidator extends AbstractCPatternValidator<CPlateNumber> {

}
