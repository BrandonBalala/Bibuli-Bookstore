/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("messageBean")
@SessionScoped
public class MessageManagedBean implements Serializable {

    private String message = "";

    /**
     * Get message
     * @return 
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Display message
     */
    public void displayMessage() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(message, ""));
    }
}
