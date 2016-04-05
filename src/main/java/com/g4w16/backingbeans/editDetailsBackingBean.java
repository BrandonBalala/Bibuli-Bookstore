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
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("detailsBB")
@SessionScoped
public class editDetailsBackingBean implements Serializable {
    ClientUtil clientUtil = new ClientUtil();
    
    private Client client;
    
    private String password;
    private String origPassword;
    @Inject
    private ClientJpaController clientJPAController;
    
    @PostConstruct
    private void clientSetCheck()
    {
        if(client == null)
          try {
              FacesContext.getCurrentInstance().getExternalContext().redirect("accountDetails.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(editDetailsBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setClient(Client client)
    {
        if(client == null)
        client = clientJPAController.findClientById(clientUtil.getUserId());
        else
        this.client = client;
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

