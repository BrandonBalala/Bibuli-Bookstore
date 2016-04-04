/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.util;

import com.g4w16.persistence.ContributorJpaController;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dan
 */
@FacesConverter("com.g4w16.jsf.util.AuthorConverter")
public class AuthorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String [] contributor=value.split(","); 
        ContributorJpaController typeJpa = CDI.current().select(ContributorJpaController.class).get();
        System.out.println(">>>>>>>>name    "+contributor[0]);
        System.out.println(">>>>>>>>author    "+contributor[1]);
        return typeJpa.findContributorByName(contributor[0], contributor[1]);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
