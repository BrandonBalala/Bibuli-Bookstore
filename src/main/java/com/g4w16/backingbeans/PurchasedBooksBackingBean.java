/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.SalesJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("purchasedBooksBB")
@SessionScoped
public class PurchasedBooksBackingBean implements Serializable {
    private Client client;
    
    @Inject
    private SalesJpaController salesController;

    @Inject
    private ClientUtil clientUtil;

    @Inject
    private ClientJpaController clientController;

    public List<Books> getPurchasedBooks() {
        List<Sales> salesList = client.getSalesList();
        
        List<Books> bookList = new ArrayList<Books>();
        for(Sales sale : salesList){
            for(SalesDetails saleDetail : sale.getSalesDetailsList()){
                bookList.add(saleDetail.getBook());
            }
        }
        
        return bookList;
    }

//    private boolean validateAuthenticated() {
//        try {
//            if (!clientUtil.isAuthenticated()) {
//                return false;
//            }
//
//            int clientID = clientUtil.getUserId();
//            Client client = clientController.findClientById(clientID);
//
//            if (client == null) {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//
//        return true;
//    }
    @PostConstruct
    public void init() {
        int clientID = clientUtil.getUserId();

        client = clientController.findClientById(clientID);
    }
}
