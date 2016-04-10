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
 * @author Ofer Nitka-Nakash
 */
@FacesValidator("emailValidator")
public class emailValidator implements Validator {
    
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
			"(\\.[A-Za-z]{2,})$";
    
    @Override
    public void validate(FacesContext context,UIComponent component,Object value) throws ValidatorException {
        if(!((String)value).matches(EMAIL_PATTERN))
        {
            FacesMessage msg = new FacesMessage("Email validation failed.", 
						"Invalid Email format.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
        }
    }
}
