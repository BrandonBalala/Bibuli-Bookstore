/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.jsf.util.MessageUtil;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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
@Named("loginBB")
@ViewScoped
public class LoginBackingBean implements Serializable {

    @Inject
    private ClientJpaController clientJPAController;

    private String email;

    private String password;

    private boolean loggedIn;

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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Action
     */
    public void login() {

        // Get Session object so that the status of the individual who just logged in
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        // Used to contain that will be displayed on the login form after Login button is pressed
        FacesMessage message;
        boolean authenticated = false;

        // Is there a client with these credentials
        Client client = clientJPAController.findClientByEmailAndPassword(email, password);

        // There is a client so login was successful
        if (client != null) {
            authenticated = true;
            message = MessageUtil.getMessage(
                    "messages", "welcome", new Object[]{email});
            message.setSeverity(FacesMessage.SEVERITY_INFO);
            session.setAttribute("client", client.getId());
        } else {
            // MessagesUtil simplifies creating localized messages
            message = MessageUtil.getMessage(
                    "messages", "loginerror", new Object[]{email});
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
        }
        // Store the outcome in the session object
        session.setAttribute("authenticated", authenticated);

        // Place the message in the context so that it will be displayed
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public String logout() {
     ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
         .getSession(false)).invalidate();
     return "/ClientPages/mainPage.xhtml";
    }
    
    
    //REMOVE LATER, USED FOR TESTING ONLY 
    //FASHFASFHOASIFHOIOASOIH
    @PostConstruct
    public void init() {
        email = "cbutler1@a8.net";
        password = "a";
        loggedIn = true;
        
    }
}
