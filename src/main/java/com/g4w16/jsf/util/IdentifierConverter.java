/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.util;

import com.g4w16.persistence.BookIdentifiersJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.IdentifierTypeJpaController;
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
        System.out.println(">>>>>>>>>>>>format" + value);
        IdentifierTypeJpaController identifierJpa = CDI.current().select(IdentifierTypeJpaController.class).get();
        //return identifierJpa.findIdentifierTypeByID(value);
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
