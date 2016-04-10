package com.g4w16.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * This is a custom validator that must implement the Validator interface. The
 * annotation provides the name that is used in the tag This implementation
 * performs the Luhn test
 *
 * @author Brandon Balala
 */
@FacesValidator("CreditCardValidator")
public class CreditCardValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) {
        if (value == null) {
            return;
        }
        String cardNumber;
        cardNumber = value.toString().replaceAll("\\D", ""); // remove
        // non-digits
        
        if (!luhnCheck(cardNumber)) {
            FacesMessage message = new FacesMessage("Invalid credit card number");
            throw new ValidatorException(message);
        }
    }

    private static boolean luhnCheck(String cardNumber) {
        int sum = 0;

        for (int i = cardNumber.length() - 1; i >= 0; i -= 2) {
            sum += Integer.parseInt(cardNumber.substring(i, i + 1));
            if (i > 0) {
                int d = 2 * Integer.parseInt(cardNumber.substring(i - 1, i));
                if (d > 9) {
                    d -= 9;
                }
                sum += d;
            }
        }

        return sum % 10 == 0;
    }
}
