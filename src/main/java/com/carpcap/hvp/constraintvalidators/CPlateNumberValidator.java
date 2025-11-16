package com.carpcap.hvp.constraintvalidators;

import com.carpcap.hvp.annotation.CPlateNumber;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;


@AutoService(ConstraintValidator.class)
public class CPlateNumberValidator extends AbstractCPatternValidator<CPlateNumber> {

}
