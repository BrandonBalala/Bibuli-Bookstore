/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Poll;
import com.g4w16.persistence.PollJpaController;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("pollBB")
@SessionScoped
public class PollBackingBean implements Serializable{
    private Poll poll;
    
    private int choice;
    private boolean resultsActive;
    
    @Inject
    private PollJpaController pollController;
    
    /**
     * Get a poll at random from all selected polls
     * @return Poll
     */
    public Poll getPoll(){
        List<Poll> polls = pollController.findSelectedPolls();
        if(polls.isEmpty()){
            this.poll = new Poll();
        }
        else
        {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(polls.size());
            this.poll = polls.get(index);
        }
        
        return poll;
    }
    
    /**
     * Get choice
     * @return choice
     */
    public int getChoice(){
        return choice;
    }
    
    /**
     * Set choice
     * @param choice 
     */
    public void setChoice(int choice){
        this.choice = choice;
    }
    
    /**
     * is the result active
     * @return resultsActive
     */
    public boolean isResultsActive(){
        return resultsActive;
    }
    
    /**
     * Set whether results is active or not
     * @param isActive 
     */
    public void setResultsActive(boolean isActive){
        this.resultsActive = isActive;
    }
    
    /**
     * Submit the result of the poll
     */
    public void submitResult(){
        try {
            pollController.updatePoll(poll.getId(), choice);
            resultsActive = true;
        } catch (Exception ex) {
            Logger.getLogger(PollBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PostConstruct
    public void init(){
        this.poll = getPoll();
        resultsActive = false;
    }
    
}
