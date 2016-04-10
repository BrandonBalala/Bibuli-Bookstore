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
import com.g4w16.jsf.util.MessageUtil;
import com.g4w16.persistence.ClientJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * Class which takes care of giving all the backing data
 * @author Ofer Nitka-Nakash
 */
@Named("loginBB")
@ViewScoped
public class LoginBackingBean implements Serializable {

    private Client client;
     HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    @Inject
    private ClientJpaController clientJPAController;

    private String email;

    private String password;
    
    @Inject
    private ClientUtil clientUtil;
    
    @Inject
    private ShoppingCartBackingBean cartBB;

    public Client getClient() {
        return client;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Login method which contains a significant portion of code found in 
     * Ken Fogel login example for JSF
     */
    public String login() {

        // Get Session object so that the status of the individual who just logged in
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        // Used to contain that will be displayed on the login form after Login button is pressed
        FacesMessage message;
        boolean authenticated = false;

        // Is there a client with these credentials
        client = clientJPAController.findClientByEmailAndPassword(email, password);

        // There is a client so login was successful
        if (client != null) {
            authenticated = true;
            session.setAttribute("client", client.getId());
        }
        // Store the outcome in the session object
        session.setAttribute("authenticated", authenticated);


        //removeOwnedBooksFromCart();
        if(!authenticated){
            FacesMessage msg = new FacesMessage("Login failed.", 
						"Invalid Credential.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                       FacesContext.getCurrentInstance().addMessage(null,msg);
        }
        else{
        return "mainPage?faces-redirect=true";
        }
        return null;
    }

    /*
    * Used to redirect to the login page if the user isn't authenticated
    */
    public void sendToLogin() throws IOException {
         if (session.getAttribute("authenticated") == null || (boolean)session.getAttribute("authenticated")!= true)
         FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
    
    /*
    * A method which invalidates the current session
    */
    public String logout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false)).invalidate();

        return "mainPage?faces-redirect=true";
    }

//    private void removeOwnedBooksFromCart() {
//        Client client = clientJPAController.findClientById(clientUtil.getUserId());
//        List<Books> bookList = cartBB.getBookList();
//        
////        salesLoop:
////        for (Sales sale : client.getSalesList()) {
////            for (SalesDetails salesDetail : sale.getSalesDetailsList()) {
////                Books temp = salesDetail.getBook();
////                if (bookList.contains(temp)) {
////                    bookList.remove(temp);
////                }
////            }
////        }
////        
////        cartBB.setBookList(bookList);
////    }
}
