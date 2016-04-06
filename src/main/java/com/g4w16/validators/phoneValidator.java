/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author BRANDON-PC
 */
@FacesValidator("phoneValidator")
public class phoneValidator implements Validator {
 private static final String PHONE_PATTERN = "^[0-9]{7})$";
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if(((String)o).matches(PHONE_PATTERN)){
              FacesMessage msg = new FacesMessage("Phone validation failed.", 
						"Invalid Phone format.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
        }
    }
    
    
}
