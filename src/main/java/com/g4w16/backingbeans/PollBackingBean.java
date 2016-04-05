/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Poll;
import com.g4w16.persistence.PollJpaController;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("pollBB")
@RequestScoped
public class PollBackingBean {
    private Poll poll;
    
    private int choice;
    private boolean resultsActive;
    
    @Inject
    private PollJpaController pollController;
    
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
    
    public int getChoice(){
        return choice;
    }
    
    public void setChoice(int choice){
        this.choice = choice;
    }
    
    public boolean isResultsActive(){
        return resultsActive;
    }
    
    public void setResultsActive(boolean isActive){
        this.resultsActive = isActive;
    }
    
    
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
