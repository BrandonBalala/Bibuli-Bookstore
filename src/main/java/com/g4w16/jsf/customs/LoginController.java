/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.customs;

import com.g4w16.entities.Client;
import com.g4w16.jsf.controller.util.MessageUtil;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ofern
 */
@Named("loginController")
@ViewScoped
public class LoginController implements Serializable {

    @Inject
    private ClientJpaController clientJPAController;

    private String email;

    private String password;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

    }

    /**
     * Action
     */
    public void login() {

        // Get Session object so that the status of the individual who just logged in
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        // Used to contain that will be displayed on the login form after Login button is pressed
        FacesMessage message;
        boolean loggedIn = false;

        // Is there a client with these credentials
        Client client = clientJPAController.findClientByEmailAndPassword(email, password);

        // There is a client so login was successful
        if (client != null) {
            loggedIn = true;
            message = MessageUtil.getMessage(
                    "messages", "welcome", new Object[]{email});
            message.setSeverity(FacesMessage.SEVERITY_INFO);
        } else {
            // Unsuccessful login
            loggedIn = false;
            // MessagesUtil simplifies creating localized messages
            message = MessageUtil.getMessage(
                    "messages", "loginerror", new Object[]{email});
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
        }
        // Store the outcome in the session object
        session.setAttribute("loggedIn", loggedIn);

        // Place the message in the context so that it will be displayed
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}