/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Sales;
import com.g4w16.persistence.SalesJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("invoiceBB")
@SessionScoped
public class InvoiceBackingBean implements Serializable {
    private Sales sale;
    private String email;
    @Inject
    private SalesJpaController salesJPAController;
    private DecimalFormat formatter = new DecimalFormat("#0.##");
    
    public DecimalFormat getFormatter()
    {
        return formatter;
    }
    public Sales getSale(){
        return sale;
    }
    
    public void setSale(Sales sale){
        this.sale = sale;
    }
    
    public void sendInvoice(Sales sale)
    {
        setSale(sale);
        
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void sendToInvoicesList() throws IOException {
         if (sale == null)
         FacesContext.getCurrentInstance().getExternalContext().redirect("my-books.xhtml");
    }
}
