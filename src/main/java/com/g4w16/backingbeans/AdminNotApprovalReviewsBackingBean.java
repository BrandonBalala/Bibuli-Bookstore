package com.g4w16.backingbeans;

import com.g4w16.entities.Client;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.Title;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 1232048
 */
@Named("notApprovalReviewBB")
@SessionScoped
public class AdminNotApprovalReviewsBackingBean implements Serializable{
      
    private List<String> status ;
    @Inject
    ReviewsJpaController reviewController;

    private List<Reviews> reviews;
    private Reviews oneReview;
    private List<Reviews> filteredReviews;
    
    @PostConstruct
    public void init() {
        reviews = reviewController.findReviewByApprovalStatus(Boolean.FALSE);
    }
    
    public List<Reviews> getReviews(){
        return reviews;
    } 
    
    public List<String> getStatus(){
        status= new ArrayList<>(Arrays.asList("TRUE","FALSE"));
        return status;
    }
    
     
    public List<Reviews> getFilteredReviews() {
        return filteredReviews;
    }
 
    public void setFilteredReviews(List<Reviews> filteredReviews) {
        this.filteredReviews = filteredReviews;
    }
    
    public void getUpdateApprovalStatus(Reviews r) throws Exception {
        oneReview=reviewController.findReviewByPK(r.getReviewsPK());
        reviewController.updateApprovalStatus(oneReview.getReviewsPK());
    }
    
    public void destroy(Reviews r)throws NonexistentEntityException, RollbackFailureException, Exception {
        oneReview=reviewController.findReviewByPK(r.getReviewsPK());
        reviewController.destroy(oneReview.getReviewsPK());
    }
    
    public int getFalseReviewsAccount(){
        return reviews.size();
    }
    
    public void onRowEdit(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Edited", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
           System.out.println(((Client) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Client Cancelled", ((Client) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
        System.out.println(((Client) event.getObject()).getId());
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

    

