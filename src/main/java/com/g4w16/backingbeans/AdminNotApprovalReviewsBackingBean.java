package com.g4w16.backingbeans;


import com.g4w16.entities.Reviews;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dan Wang
 */
@Named("notApprovalReviewBB")
@SessionScoped
public class AdminNotApprovalReviewsBackingBean implements Serializable{
      
    
    @Inject
    ReviewsJpaController reviewController;
    
    private List<String> status ;
    private List<Reviews> reviews;
    private Reviews oneReview;
    private List<Reviews> filteredReviews;
    
    @PostConstruct
    public void init() {
        reviews = reviewController.findReviewByApprovalStatus(Boolean.FALSE);
        status= new ArrayList<>(Arrays.asList("TRUE","FALSE"));
    }
    
    public List<Reviews> getReviews(){
        return reviews;
    } 
    
    public List<String> getStatus(){
        
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
    
    public int getFalseReviewsCount(){
        return reviews.size();
    }
    
    /**
     * Change approval status
     * @param event
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        Reviews editedReviw = (Reviews)event.getObject();
        reviewController.edit(editedReviw);
        init();
    }
     
    public void onRowCancel(RowEditEvent event) {
    }   
}