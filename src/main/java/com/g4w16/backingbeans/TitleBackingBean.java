/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Title;
import com.g4w16.persistence.TitleJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ofern
 */
@Named("titleBB")
@SessionScoped
public class TitleBackingBean implements Serializable{
    @Inject
    private TitleJpaController titleJpaController;
    
    private Title title;
    
    public Title getClient(){
        if(title == null)
            title = new Title();
        
        return title;
    }
    
    public List<Title> getTitles(){
        return titleJpaController.findTitleEntities();
    }
}
