/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Sales;
import com.g4w16.jsf.util.ResponseCatcher;
import com.g4w16.persistence.SalesJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        this.email = renderView("invoice");
    }
    
    public String getEmail()
    {
        return email;
    }
    /**
     * https://developer.jboss.org/message/580938#580938
     * @param template
     * @return 
     */
    public String renderView(String template) {
            FacesContext faces = FacesContext.getCurrentInstance();
            ExternalContext context = faces.getExternalContext();
           
            HttpServletResponse response = (HttpServletResponse)
                    context.getResponse();
            ResponseCatcher catcher = new ResponseCatcher(response);
            try {
            ViewHandler views = faces.getApplication().getViewHandler();
            // render the message
                context.setResponse(catcher);
                context.getRequestMap().put("emailClient", true);
                views.renderView(faces, views.createView(faces, template));
                context.getRequestMap().remove("emailClient");
                context.setResponse(response);
            } catch (IOException ioe) {
                String msg = "Failed to render email internally";
                faces.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, msg, msg));
                return null;
            }
            return catcher.toString();
        }
    
    public void sendToInvoicesList() throws IOException {
         if (sale == null)
         FacesContext.getCurrentInstance().getExternalContext().redirect("my-books.xhtml");
    }
}
