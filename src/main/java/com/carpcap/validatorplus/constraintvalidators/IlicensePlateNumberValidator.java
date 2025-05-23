package com.carpcap.validatorplus.constraintvalidators;

import com.carpcap.validatorplus.annotation.IlicensePlateNumber;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;


@AutoService(ConstraintValidator.class)
public class IlicensePlateNumberValidator extends AbstractIPatternValidator<IlicensePlateNumber> {

}
