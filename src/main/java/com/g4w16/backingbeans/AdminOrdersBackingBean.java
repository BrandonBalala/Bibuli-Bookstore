/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.SalesDetailsJpaController;
import com.g4w16.persistence.SalesJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Annie So
 */
@Named("orderBB")
@SessionScoped
public class AdminOrdersBackingBean implements Serializable {

    @Inject
    SalesJpaController salesController;

    @Inject
    SalesDetailsJpaController detailsController;

    private List<Sales> sales;
    private Sales selectedSale;
    private SalesDetails selectedDetail;

    @PostConstruct
    public void init() {
        System.out.println("initializing");
        sales = salesController.findSalesEntities();
    }
    
    public int getOrderCount(){
        return salesController.getSalesCount();
    }

    public List<Sales> getSales() {
        return salesController.findSalesEntities();
    }

    public List<SalesDetails> getDetails() {
        if (selectedSale != null) {
            return salesController.findSales(selectedSale.getId()).getSalesDetailsList();
        } else {
            return new ArrayList<>();
        }
    }

    public Sales getSelectedSale() {
        return selectedSale;
    }

    public void setSelectedSale(Sales selectedSale) {
        this.selectedSale = selectedSale;
    }

    public SalesDetails getSelectedDetail() {
        return selectedDetail;
    }

    public void setSelectedDetail(SalesDetails selectedDetail) {
        if (selectedDetail == null) {
            System.out.println("null selection");
        } else {
            System.out.println("good selection");
        }
        this.selectedDetail = selectedDetail;
    }

    public void removeSale(Sales sale) throws RollbackFailureException, Exception {
        if (!sale.getRemoved()) {
            salesController.removeSale(sale.getId());
        }
    }

    public void removeSalesDetail(SalesDetails detail) throws RollbackFailureException, Exception {
        System.out.println("In remove");
        if (!detail.getRemoved()) {
            System.out.println("Removing");
            detailsController.removeSalesDetail(detail.getId());
        } else {
            System.out.println("Not removing");
        }
    }

    public void onRowSelect(SelectEvent event) {
        setSelectedSale((Sales) event.getObject());
    }

}
