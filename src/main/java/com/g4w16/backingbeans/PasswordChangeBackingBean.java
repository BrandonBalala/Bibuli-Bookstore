/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ofern
 */
@Named("passwordChangeBB")
@ViewScoped
public class PasswordChangeBackingBean implements Serializable {
    
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    
    private Client client;
    
    @Inject
    private ClientJpaController clientJPAController;
     
    @PostConstruct 
    public void fetchClient(){
        client = clientJPAController.findClientById((int)session.getAttribute("client"));
    }
    
    public Client getClient(){
        return client;
    }
     public String changePassword(){
        try {
            clientJPAController.edit(client);
        } catch (Exception ex) {
            Logger.getLogger(PasswordChangeBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            //TODO ADD ON SCREEN MESSAGE STATING ERROR OCCURED.
        }
         return "index";
     }
}
