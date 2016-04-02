/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Sales;
import com.g4w16.persistence.SalesJpaController;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("invoiceBB")
@ViewScoped
public class InvoiceBackingBean implements Serializable {
    private Sales sale;
    @Inject
    private SalesJpaController salesJPAController;
    
    public Sales getSale(){
        if(sale == null)
        {
            sale = salesJPAController.findSalesEntities(1,0).get(0);
        }
        return sale;
    }
}
