/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.util;

import com.g4w16.entities.BookIdentifiersPK;
import com.g4w16.persistence.BookIdentifiersJpaController;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dan
 */
@FacesConverter("com.g4w16.jsf.util.IdentifierConverter")
public class IdentifierConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println(">>>>>>>>>>>>identifier" + value);
        String[] str=value.split(",");
        System.out.println(">>>>>>>>>>>>identifier" + str[0]);
        System.out.println(">>>>>>>>>>>>identifier" + str[1]);
        BookIdentifiersJpaController identifierJpa = CDI.current().select(BookIdentifiersJpaController.class).get();
        return identifierJpa.findBookIdentifierByID(new BookIdentifiersPK(Integer.parseInt(str[0]),str[1]));
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
