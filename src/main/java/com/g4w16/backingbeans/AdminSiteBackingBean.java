/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.Poll;
import com.g4w16.entities.Reviews;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.PollJpaController;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dan
 */
@Named("AdminSiteBB")
@RequestScoped
public class AdminSiteBackingBean {
    @Inject
    Reviews review;

    @Inject
    ReviewsJpaController reviewController;
    
    @Inject
    Client client;
    
    @Inject
    ClientJpaController clientJpaController;
    
    
    @Inject 
    Books book;
    
    @Inject 
    BooksJpaController booksJpaController;
    
    @Inject
    Poll poll;
    
    @Inject
    PollJpaController pollJpaController;
    /**
     * For Reviews pages
     * 
     */
    public List<Reviews> getAllReviews(){
        return reviewController.findAllReviews();
    }
    
    public List<Reviews> getFalseReviews() {
        return reviewController.findReviewByApprovalStatus(Boolean.FALSE);
    }

    public void getUpdateApprovalStatus(Reviews r) throws Exception {
        review=reviewController.findReviewByPK(r.getReviewsPK());
        reviewController.updateApprovalStatus(review.getReviewsPK());
    }
    
    public void destroy(Reviews r)throws NonexistentEntityException, RollbackFailureException, Exception {
        review=reviewController.findReviewByPK(r.getReviewsPK());
        reviewController.destroy(review.getReviewsPK());
    }
    
    public int getFalseReviewsAccount(){
        return getFalseReviews().size();
    }
    
    public int getAllReviewsAccount(){
        return reviewController.getReviewsCount();
    }
    
    /**
     * For client page
     * 
     */
    public List<Client> getAllClients(){
        return clientJpaController.findAllClients();
    } 
    
    public int getClientCount(){
        return clientJpaController.getClientCount();
    }
    
    /**
     * For Inventory page
     */
    public List<Books> getAllBooks() {
        return booksJpaController.findAllBooks();
    }
    
    public int getBooksCount() {
        return booksJpaController.getBooksCount();
    }
    
    /**
     * For Survey page
     */
    public List<Poll> getAllPolls(){
        return pollJpaController.findAllPolls();
    }
    
    public int getPollCount() {
        return pollJpaController.getPollCount();
    }
    
    public int getTotal(Poll p){
        poll=pollJpaController.findPollByID(p.getId());
        int total= pollJpaController.getAnswerCount(poll.getId());
        return total;
    }
    
    public double getFirstPercentage(Poll p){
        poll=pollJpaController.findPollByID(p.getId());
        if(getTotal(poll)!=0){
        double percentage=(double)(Math.round(poll.getFirstCount()/getTotal(poll)));
        return percentage;
        }
        else
            return 0;
    }
    
    public double getSecondPercentage(Poll p){
        poll=pollJpaController.findPollByID(p.getId());
        if(getTotal(poll)!=0){
        double percentage=(double)(Math.round(poll.getSecondCount()/getTotal(poll)));
        return percentage;
        }
        else
            return 0;
    }
    
}
