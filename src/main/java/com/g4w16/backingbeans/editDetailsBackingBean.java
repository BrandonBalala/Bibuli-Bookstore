/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("detailsBB")
@ViewScoped
public class editDetailsBackingBean implements Serializable {
    ClientUtil clientUtil = new ClientUtil();
    
    private Client client;
    
    private String password;
    private String origPassword;
    @Inject
    private ClientJpaController clientJPAController;
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    @PostConstruct
    public void setClient()
    {
        client = clientJPAController.findClientById(clientUtil.getUserId());
        origPassword = client.getPassword();
    }
    
    public String getPassword()
    {
        return password;
    }
    
     public Client getClient()
    {
        return client;
    }
    
    public void saveChanges()
    {
        if(this.origPassword.equals(this.password))
            try {
                clientJPAController.edit(client);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(editDetailsBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(editDetailsBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(editDetailsBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

