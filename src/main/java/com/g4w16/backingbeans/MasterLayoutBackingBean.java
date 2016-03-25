package com.g4w16.backingbeans;

import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Brandon Balala
 */
@Named("masterLayoutBB")
@SessionScoped
public class MasterLayoutBackingBean implements Serializable {

    private List<Genre> genreList;
    private List<Format> formatList;
    
    @Inject
    private GenreJpaController genreController;

    @Inject
    private FormatJpaController formatController;
    
    @Inject
    private BooksJpaController bookController;
    
    @Inject
    private ResultBackingBean resulBB;
    
    public List<Genre> getGenreList(){
        if(genreList == null){
            genreList = new ArrayList<Genre>();
        }
        
        return genreList;
    }
    
    public void setGenreList(List<Genre> genreList){
        this.genreList = genreList;
    }
    
    public List<Format> getFormatList(){
        if(formatList == null){
            formatList = new ArrayList<Format>();
        }
        
        return formatList;
    }
    
    public void setFormatList(List<Format> formatList){
        this.formatList = formatList;
    }
    
    @PostConstruct
    public void init() {
        genreList = genreController.findAllGenres();
        formatList = formatController.findAllFormats();
    }
    
    public String displayAllBooks(){
        resulBB.setBookList(bookController.findAllBooks());
        
        return "results";
    }
    
    
}
