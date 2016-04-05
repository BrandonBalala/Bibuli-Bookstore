package com.g4w16.locale;

import java.util.Locale;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author Paul
 */

@Named
@SessionScoped
public class LocaleBean {

	private Locale locale;

	@PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

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
