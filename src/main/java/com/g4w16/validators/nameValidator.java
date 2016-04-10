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
 * @author ofern
 */
@FacesValidator("nameValidator")
public class nameValidator implements Validator{
    
     @Override
    public void validate(FacesContext context,UIComponent component,Object value) throws ValidatorException {
        if(((String)value).isEmpty())
        {
            FacesMessage msg = new FacesMessage("Name Validation Failed.", 
						"A name must be given.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
        }
    }
}
