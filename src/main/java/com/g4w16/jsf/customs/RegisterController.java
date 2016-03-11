/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.customs;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("registerController")
@ViewScoped
public class RegisterController implements Serializable{
    
    @Inject
    private ClientJpaController clientJpaController;
    
    private Client client;
    
    public Client getClient(){
        if(client == null)
            client = new Client();
        
        return client;
    }
    
    public String register() throws Exception {
        clientJpaController.create(client);
        return "login";
    }
    
    
}