/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Reviews;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dan Wang
 */
@Named("reviewBB")
@SessionScoped
public class AdminReviewsBackingBean implements Serializable{
        
    @Inject
    ReviewsJpaController reviewController;

    private List<Reviews> reviews;
    private Reviews oneReview;
    private List<Reviews> filteredReviews;
    
    @PostConstruct
    public void init() {
        reviews = reviewController.findAllReviews();
    }
    
    public List<Reviews> getReviews(){
        return reviews;
    } 
     
    public List<Reviews> getFilteredReviews() {
        return filteredReviews;
    }
 
    public void setFilteredClients(List<Reviews> filteredReviews) {
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
    
    public int getAllReviewsAccount(){
        return reviewController.getReviewsCount();
    }
}
