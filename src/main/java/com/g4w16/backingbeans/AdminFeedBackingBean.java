/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Feed;
import com.g4w16.persistence.FeedJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangdan
 */
@Named("feedBB")
@RequestScoped
public class AdminFeedBackingBean implements Serializable { 
    private List<Feed> feeds;
    private List<Integer> ids ;
    private int feedId;
    private List<Feed> filteredFeeds;
    
    @Inject
    FeedJpaController feedJpaController;
    
    @PostConstruct
    public void init() {
        feeds = feedJpaController.findAllFeeds();
    }
    
    public List<Feed> getFeeds(){
        return feeds;
    }
    
    public int getFeedCount(){
        return feedJpaController.getFeedCount();
    }
    
    public List<Integer> getIds() {
        ids=new ArrayList<>();
         for(int i=0;i<feeds.size();i++){
            ids.add(i+1);
         }
         return ids;
    }
    
    public int getFeedId() {
        return feedId;
    }
 
    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }
    
    public List<Feed> getFilteredFeeds() {
        return filteredFeeds;
    }
 
    public void setFilteredFeeds(List<Feed> filteredFeeds) {
        this.filteredFeeds = filteredFeeds;
    }
    
    public void onRowEdit(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Edited", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    //       System.out.println(((Client) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Cancelled", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    //    System.out.println(((Client) event.getObject()).getId());
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
}
