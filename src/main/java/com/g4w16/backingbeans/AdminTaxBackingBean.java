/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import java.io.Serializable;
import java.util.List;
import com.g4w16.entities.TaxeRates;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.g4w16.persistence.TaxeRatesJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import javax.annotation.PostConstruct;
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
    
    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        taxeRatesJpaController.edit((TaxeRates) event.getObject());
    }
     
    public void onRowCancel(RowEditEvent event) {
    }
        
}
