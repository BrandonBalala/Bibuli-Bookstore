/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.util;

import com.g4w16.persistence.TitleJpaController;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dan
 */
@FacesConverter("com.g4w16.jsf.util.FormatConverter")
public class TitleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TitleJpaController titleJpa = CDI.current().select(TitleJpaController.class).get();
        return titleJpa.findTitle(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();

    }

}
