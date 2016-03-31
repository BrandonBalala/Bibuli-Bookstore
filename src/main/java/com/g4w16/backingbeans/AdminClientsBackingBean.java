/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import com.g4w16.entities.Client;
import com.g4w16.entities.Title;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.TitleJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author 1232048
 */
@Named("clientBB")
@RequestScoped
public class AdminClientsBackingBean implements Serializable {   
    @Inject
    ClientJpaController clientJpaController;
     
    @Inject
    TitleJpaController titleJpaController;
    
    private List<Client> clients;
    private List<Client> filteredClients;
    
    @PostConstruct
    public void init() {
        clients = clientJpaController.findAllClients();
    }
    
    public List<Client> getClients(){
        return clients;
    } 
    
    public int getClientCount(){
        return clientJpaController.getClientCount();
    }

    public List<Client> getFilteredClients() {
        return filteredClients;
    }
 
    public void setFilteredClients(List<Client> filteredClients) {
        this.filteredClients = filteredClients;
    }
    
    public void onRowEdit(RowEditEvent event) throws NonexistentEntityException, RollbackFailureException, Exception {
        Client editedClient = (Client) event.getObject();
        clientJpaController.edit(editedClient);
    }
     
    public void onRowCancel(RowEditEvent event) {
    }
    
   
    
    public List<Title> getTitles() {
        return titleJpaController.findTitleEntities();
    }
    
        
    
    
   

}
