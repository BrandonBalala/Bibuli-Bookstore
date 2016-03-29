/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Poll;
import java.io.Serializable;
import java.util.List;
import com.g4w16.entities.TaxeRates;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.g4w16.persistence.TaxeRatesJpaController;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author 1232048
 */
@Named("taxBB")
@RequestScoped
public class AdminTaxBackingBean implements Serializable { 
    private List<TaxeRates> tax;
    private List<TaxeRates> filteredTaxeRates;
    
    @Inject
    TaxeRatesJpaController taxeRatesJpaController;
    
    @PostConstruct
    public void init() {
        tax = taxeRatesJpaController.findTaxeRatesEntities();
    }
    
    public List<TaxeRates> getTaxeRates(){
        return tax;
    }
    
    public int getTaxeRatesCount(){
        return taxeRatesJpaController.getTaxeRatesCount();
    }
    
    public List<TaxeRates> getFilteredTaxeRates() {
        return filteredTaxeRates;
    }
 
    public void setFilteredTaxeRates(List<TaxeRates> filteredTaxeRates) {
        this.filteredTaxeRates = filteredTaxeRates;
    }
    
    public void onRowEdit(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Edited", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    //       System.out.println(((Client) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Cancelled", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    //    System.out.println(((Client) event.getObject()).getId());
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    
    
    
    
    
}
