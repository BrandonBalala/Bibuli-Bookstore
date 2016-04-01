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
 *
 * @author ofern
 */
@RequestScoped
public class ClientUtil {

    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public int getUserId() {
        return (int) session.getAttribute("client");
    }

    public boolean isAuthenticated() {
        return (boolean) session.getAttribute("authenticated");
    }
}
