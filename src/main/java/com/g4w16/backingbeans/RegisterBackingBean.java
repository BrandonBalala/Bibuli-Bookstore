/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class which holds all the backing data and actions for registration of users
 * @author Ofer Nitka-Nakash
 */
@Named("registerBB")
@ViewScoped
public class RegisterBackingBean implements Serializable{
    
    @Inject
    private ClientJpaController clientJpaController;
    
    private Client client;
    
    public Client getClient(){
        if(client == null)
            client = new Client();
        
        return client;
    }
    
    /*
    * Persists the Client to database and redirects to the login page.
    */
    public String register() throws Exception {
        clientJpaController.create(client);
        
        return "login?faces-redirect=true";
    }
    
    
}