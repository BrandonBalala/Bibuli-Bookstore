/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

/**
 * A backing bean which holds all the locale data and actions
 * @author Ofer Nitka-Nakash
 */
@Named("LocaleBB")
@SessionScoped
public class LocaleBackingBean implements Serializable{
   private static final long serialVersionUID = 1L;
   private String locale;

   private static Map<String,Object> countries;
   
   /*
   * a static declaration block which states all the 
   * supported locales.
   */
   static{
      countries = new HashMap<String,Object>();
      countries.put("English", Locale.ENGLISH);
      countries.put("Français", Locale.FRENCH);
   }

   public Map<String, Object> getCountries() {
      return countries;
   }

   public String getLocale() {
      return locale;
   }

   public void setLocale(String locale) {
      this.locale = locale;
   }

   /*
   * a value changed action which takes care of changing the local in the context 
   * instance to the newly selected value
   */
   public void ValueChanged(ValueChangeEvent e){
      String newLocaleValue = e.getNewValue().toString();
      for (Map.Entry<String, Object> entry : countries.entrySet()) {
         if(entry.getValue().toString().equals(newLocaleValue)){
            FacesContext.getCurrentInstance()
               .getViewRoot().setLocale((Locale)entry.getValue());         
         }
      }
   }
}
