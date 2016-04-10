/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * A class to provide some methods which avoid client session based code rewriting
 * @author Ofer Nitka-Nakash
 */
@RequestScoped
public class ClientUtil {

    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    /*
    * A method which returns the logged in clients Id
    */
    public int getUserId() {
        return (int) session.getAttribute("client");
    }

    /*
    * A method which return whether a user has been authenticated
    */
    public boolean isAuthenticated() {
        boolean authentic = false;
        try {
            authentic = (boolean) session.getAttribute("authenticated");
        } catch (Exception e) {
            return false;
        }

        return authentic;
    }
}
