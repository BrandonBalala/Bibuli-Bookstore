/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Client;
import com.g4w16.entities.Title;
import com.g4w16.persistence.BillingAddressJpaController;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.SalesJpaController;
import com.g4w16.persistence.TitleJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author 1232048
 * @author Annie So
 */
@Named("clientBB")
@RequestScoped
public class AdminClientsBackingBean implements Serializable {   
    @Inject
    private ClientJpaController clientJpaController;
     
    @Inject
    private TitleJpaController titleJpaController;
    
    @Inject
    private SalesJpaController salesController;
    
    @Inject
    private BillingAddressJpaController addressController;
    
    private List<Client> clients;
    private List<Client> filteredClients;
    private Client selectedClient;
    private BillingAddress selectedAddress;
    
    @PostConstruct
    public void init() {
        clients = clientJpaController.findAllClients();
    }
    
    public List<Client> getClients(){
        return clients;
    }
    
    public Client getSelectedClient(){
        return selectedClient;
    }
    
    public void setSelectedClient(Client selectedClient){
        this.selectedClient = selectedClient;
    }
    
    public BillingAddress getSelectedAddress(){
        return selectedAddress;
    }
    
    public void setSelectedAddress(BillingAddress selectedAddress){
        this.selectedAddress = selectedAddress;
    }
    /**
     * Display Billing Address of selected client
     * @return 
     */
    public List<BillingAddress> getBillingAddress() {
        if (selectedClient != null) {
            return clientJpaController.findClientById(selectedClient.getId()).getBillingAddressList();
        } else {
            return new ArrayList<>();
        }
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
    
    /**
     * Edit a record
     * @param event
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void onClientEdit(RowEditEvent event) throws NonexistentEntityException, RollbackFailureException, Exception {
        Client editedClient = (Client) event.getObject();
        clientJpaController.edit(editedClient);
    }
     
    public void onRowCancel(RowEditEvent event) {
    }
    
    public List<Title> getTitles() {
        return titleJpaController.findTitleEntities();
    }
    
    public BigDecimal getTotalSales(Integer clientId){
        return salesController.getTotalSalesForClient(clientId);
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("row selected: " + ((Client)event.getObject()).getId());
        setSelectedClient((Client) event.getObject());
    }
}
