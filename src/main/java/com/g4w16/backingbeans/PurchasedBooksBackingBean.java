/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.SalesJpaController;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Brandon Balala, Ofer Nitka-Nakash
 */
@Named("purchasedBooksBB")
@SessionScoped
public class PurchasedBooksBackingBean implements Serializable {

    private Client client;

    @Inject
    private SalesJpaController salesController;

    @Inject
    private LoginBackingBean loginBean;

    @Inject
    private ClientUtil clientUtil;

    @Inject
    private ClientJpaController clientController;

    @Inject
    private InvoiceBackingBean invoiceBB;

    /**
     * Get purchased books
     *
     * @return bookList
     */
    public List<Books> getPurchasedBooks() {
        setClient();
        List<Sales> salesList;
        if (client != null) {
            salesList = client.getSalesList();
        } else {
            salesList = new ArrayList();
        }

        List<Books> bookList = new ArrayList<Books>();
        for (Sales sale : salesList) {
            for (SalesDetails saleDetail : sale.getSalesDetailsList()) {
                bookList.add(saleDetail.getBook());
            }
        }

        return bookList;
    }

    /**
     * Get sales list
     *
     * @return List<Sales>
     */
    public List<Sales> getSalesList() {
        if (client != null) {
            return client.getSalesList();
        } else {
            return new ArrayList();
        }
    }

    /**
     * Get formatted dates
     *
     * @param date
     * @return String
     */
    public String getFormattedDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    /**
     * Display invoice for particular book
     *
     * @param sale
     * @return String
     */
    public String displayInvoice(Sales sale) {
        invoiceBB.setSale(sale);
        return "invoice";
    }

    /**
     * Set client
     */
    public void setClient() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (client == null && session.getAttribute("authenticated") != null) {
            int clientID = clientUtil.getUserId();

            client = clientController.findClientById(clientID);
        }
    }

    /**
     * Method in charge of downloading a book
     * @param book
     * @throws IOException
     */
    public void downloadBook(String book) throws IOException {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/books/" + book);
        byte[] buf = new byte[stream.available()];
        int offset = 0;
        int numRead = 0;
        while ((offset < buf.length) && ((numRead = stream.read(buf, offset, buf.length - offset)) >= 0)) {
            offset += numRead;
        }
        stream.close();
        HttpServletResponse response
                = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=" + book);
        response.getOutputStream().write(buf);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
    }
}
