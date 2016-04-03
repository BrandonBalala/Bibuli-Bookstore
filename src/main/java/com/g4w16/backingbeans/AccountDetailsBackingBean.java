package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ofern
 */
@Named("AccountDetailsBB")
@SessionScoped
public class AccountDetailsBackingBean implements Serializable{
    ClientUtil clientUtil = new ClientUtil();
    
    private Client client;
    @Inject
    private ClientJpaController clientJPAController;
    @Inject
    private editDetailsBackingBean editDetails;
    
    @PostConstruct
    public void setClient()
    {
        client = clientJPAController.findClientById(clientUtil.getUserId());
    }
    
     public Client getClient()
    {
        return client;
    }
     
    public String editAccount()
    {
        editDetails.setClient(client);
        return "editDetails";
    }
}
