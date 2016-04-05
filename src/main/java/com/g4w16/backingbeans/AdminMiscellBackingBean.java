/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Genre;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.g4w16.persistence.GenreJpaController;

import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;


/**
 *
 * @author 1232048
 */
@Named("miscellBB")
@RequestScoped
public class AdminMiscellBackingBean implements Serializable { 
    @Inject
    GenreJpaController genreJpaController;
    private String newGenre;
    private List<Genre> allGenre;
    private List<Genre> selectedGenre;

    
    @PostConstruct
    public void init() {
        allGenre = genreJpaController.findAllGenres();
        selectedGenre=new ArrayList<>();
    }
    
    /**************************Genre*********************************/
      public List<Genre> getAllGenre() {
        return allGenre;
    }
    
    public List<Genre> getSelectedGenre() {
        return selectedGenre;
    }

    public void setSelectedGenre(List<Genre> selectedGenre) {
        this.selectedGenre = selectedGenre;
    }
    
    public String getNewGenre() {
        return newGenre;
    }
    
    public void setNewGenre(String newGenre) {
        this.newGenre = newGenre;
    }
    
    public void addGenre() throws RollbackFailureException, Exception {
        try{
        Genre g=new Genre();
        g.setType(newGenre);
        genreJpaController.create(g);
        init();
        newGenre="";
        }
        catch(RollbackFailureException rfe){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, rfe.getMessage(), rfe.getMessage()));
        }
        catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

        public void deleteGenre(List<Genre> selected) throws RollbackFailureException, Exception{
           for(Genre g:selected){
            genreJpaController.destroy(g.getType());
           }        
           init();
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }
    
    
    
   
    
    
    
}
