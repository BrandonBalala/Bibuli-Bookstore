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
 * Class which takes care of giving all the backing data
 * @author Ofer Nitka-Nakash
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
     * Login method which contains a significant portion of code found in 
     * Ken Fogel login example for JSF
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
            session.setAttribute("admin", admin.getUsername());
        } 
        // Store the outcome in the session object
        session.setAttribute("adminAuthenticated", authenticated);
        if(!authenticated){
            FacesMessage msg = new FacesMessage("Login failed.", 
						"Invalid Credential.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                       FacesContext.getCurrentInstance().addMessage(null,msg);
        }
        else{
        return "admin_home?faces-redirect=true";
        }
        return null;

        
    }

    /*
    * A method which invalidates the current session
    */
    public void logout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false)).invalidate();
    }
    
    /*
    * Used to redirect to the login page if the user isn't authenticated
    */
     public void sendToLogin() throws IOException {
         HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
         if (session.getAttribute("adminAuthenticated")== null || (boolean)session.getAttribute("adminAuthenticated")!= true)
         FacesContext.getCurrentInstance().getExternalContext().redirect("admin_login.xhtml");
    }
}
