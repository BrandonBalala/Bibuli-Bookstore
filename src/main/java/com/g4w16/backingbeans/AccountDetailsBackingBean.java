package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.persistence.ClientJpaController;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/*
 * AccountDetailsBackingBean takes care of providing the backing data for the accounts detail
 * page and allows for the saving of edits done to the client entry
 */

/**
 *
 * @author Ofer Nitka-Nakash
 */
@Named("AccountDetailsBB")
@SessionScoped
public class AccountDetailsBackingBean implements Serializable{
    @Inject
    ClientUtil clientUtil;
    
    private Client client;
    @Inject
    private ClientJpaController clientJPAController;
    @Inject
    private editDetailsBackingBean editDetails;
    
    public void setClient()
    {
        if(clientUtil.isAuthenticated())
        client = clientJPAController.findClientById(clientUtil.getUserId());
    }
    
     public Client getClient()
    {
        return client;
    }
     
     /*
     *editAccount saves the edits done to the client entity 
     */
    public String editAccount()
    {
        editDetails.setClient(client);
        return "editDetails";
    }
}
