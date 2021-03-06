/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Admin;
import com.g4w16.persistence.AdminJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author wangdan
 * @author Annie So
 */
@Named("managementBB")
@RequestScoped
public class AdminManagement implements Serializable {

    @Inject
    AdminJpaController adminJpaController;

    private String newName;
    private String newPassword;
    private List<Admin> allAdmin;
    private List<Admin> selectedAdmin;

    @PostConstruct
    public void init() {
        allAdmin = adminJpaController.findAdminEntities();
        selectedAdmin = new ArrayList<>();
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<Admin> getAllAdmin() {
        return allAdmin;
    }

    public void setAllAdmin(List<Admin> allAdmin) {
        this.allAdmin = allAdmin;
    }

    public List<Admin> getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(List<Admin> selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public int getAdminCount() {
        return adminJpaController.getAdminCount();
    }
    /**
     * Add a new record, with validation for the length and already exist
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void addAdmin() throws RollbackFailureException, Exception {
        boolean noErrors = true;

        if (newName.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name cannot be longer than 128 characters", "Name cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (newPassword.length() > 254) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password cannot be longer than 128 characters", "Password cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Admin admin : allAdmin) {
            if (admin.getUsername().equalsIgnoreCase(newName)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An admin with that name already exists", "An admin with that name already exists"));
                noErrors = false;
                break;
            }
        }

        if (noErrors) {
            try {
                Admin a = new Admin();
                a.setUsername(newName);
                a.setPassword(newPassword);
                adminJpaController.create(a);
                init();
                newName = "";
                newPassword = "";
            } catch (RollbackFailureException rfe) {
                newName = "";
                newPassword = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception e) {
                newName = "";
                newPassword = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }
    
    /**
     * delete a record
     * @param selected 
     */
    public void deleteAdmin(List<Admin> selected) {
        try {
            for (Admin a : selected) {
                adminJpaController.destroy(a.getUsername());
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
            }
        } catch (RollbackFailureException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));

        }
    }
}
