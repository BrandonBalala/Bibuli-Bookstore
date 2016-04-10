/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Feed;
import com.g4w16.persistence.FeedJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangdan
 * @author Annie So
 */
@Named("feedBB")
@RequestScoped
public class AdminFeedBackingBean implements Serializable {

    private List<Feed> feeds;
    private List<Integer> ids;
    private int feedId;
    private List<Feed> filteredFeeds;
    private Feed selected;
    private String uri;
    private String name;

    @Inject
    FeedJpaController feedJpaController;

    @PostConstruct
    public void init() {
        feeds = feedJpaController.findAllFeeds();
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public int getFeedCount() {
        return feedJpaController.getFeedCount();
    }

    public Feed getSelected() {
        return selected;
    }

    public void setSelected(Feed selected) {
        this.selected = selected;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIds() {
        ids = new ArrayList<>();
        for (int i = 0; i < feeds.size(); i++) {
            ids.add(i + 1);
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

    /**
     * Edit one record
     * @param event 
     */
    public void onRowEdit(RowEditEvent event) {
        try {
            feedJpaController.edit((Feed) event.getObject());
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminFeedBackingBean.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(AdminFeedBackingBean.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }
    /**
     * Edit is cancel
     * @param event 
     */
    public void onRowCancel(RowEditEvent event) {
    }
    
    /**
     * Change selected status
     * @param f
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void changeStatus(Feed f) throws RollbackFailureException, Exception {
        selected = feedJpaController.findFeedByID(f.getId());
        selected.setSelected(f.getSelected());
        feedJpaController.edit(selected);
    }
    
    /**
     * Add a new record
     * @param name
     * @param uri 
     */
    public void addAction(String name, String uri) {
        boolean noErrors = true;
        if (name.length() > 254) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name cannot be longer than 254 characters", "Name cannot be longer than 254 characters"));
            noErrors = false;
        }
        if (uri.length() > 254) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "URI cannot be longer than 254 characters", "URI cannot be longer than 254 characters"));
            noErrors = false;
        }
        if (noErrors) {
            try {
                Feed f = new Feed();
                f.setName(name);
                f.setUri(uri);
                f.setSelected(false);
                feedJpaController.create(f);
                init();
                uri = "";
                name = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception ex) {
                uri = "";
                name = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
            }
        }
    }
}
