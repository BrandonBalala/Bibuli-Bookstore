package com.g4w16.locale;

import java.io.Serializable;
import java.util.Locale;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Paul
 */

@Named
@SessionScoped
public class LocaleChanger implements Serializable {

    public String frenchAction() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.CANADA_FRENCH);
        return null;
    }

    public String englishAction() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.getViewRoot().setLocale(Locale.CANADA);
        return null;
    }
}
