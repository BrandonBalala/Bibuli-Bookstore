/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Admin;
import com.g4w16.jsf.util.MessageUtil;
import com.g4w16.persistence.AdminJpaController;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ofern
 */
@Named("AdminLoginBB")
@SessionScoped
public class AdminLoginBackingBean  implements Serializable{
      @Inject
    private AdminJpaController adminJPAController;

    private String username;

    private String password;
    
    @Inject
    private ShoppingCartBackingBean cartBB;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public String login() {

        // Get Session object so that the status of the individual who just logged in
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        // Used to contain that will be displayed on the login form after Login button is pressed
        FacesMessage message;
        boolean authenticated = false;

        // Is there a client with these credentials
        Admin admin = adminJPAController.findAdminByEmailAndPassword(username, password);

        // There is a client so login was successful
        if (admin != null) {
            authenticated = true;
            message = MessageUtil.getMessage(
                    "messages", "welcome", new Object[]{username});
            message.setSeverity(FacesMessage.SEVERITY_INFO);
            session.setAttribute("admin", admin.getUsername());
        } else {
            // MessagesUtil simplifies creating localized messages
            message = MessageUtil.getMessage(
                    "messages", "loginerror", new Object[]{username});
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
        }
        // Store the outcome in the session object
        session.setAttribute("adminAuthenticated", authenticated);

        // Place the message in the context so that it will be displayed
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "admin_home?faces-redirect=true";
    }

    public void logout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false)).invalidate();
    }
    
     public void sendToLogin() throws IOException {
         HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
         if (session.getAttribute("adminAuthenticated")== null || (boolean)session.getAttribute("adminAuthenticated")!= true)
         FacesContext.getCurrentInstance().getExternalContext().redirect("admin_login.xhtml");
    }
}
