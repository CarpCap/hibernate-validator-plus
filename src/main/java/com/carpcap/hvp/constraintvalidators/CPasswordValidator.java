package com.carpcap.hvp.constraintvalidators;


import com.carpcap.hvp.annotation.CPassword;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;

/**
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CPasswordValidator extends AbstractCPatternValidator<CPassword> {


}


