/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Poll;
import com.g4w16.persistence.PollJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @Inject
    private PollJpaController pollController;
    
    public Poll getPoll(){
        pollController.findSelectedPolls()
        if(poll == null){
            poll = new Poll();
        }
        
        return poll;
    }
    
    public void setPoll(Poll poll){
        this.poll = poll;
    }
    
    public int getChoice(){
        return choice;
    }
    
    public void setChoice(int choice){
        this.choice = choice;
    }
    
    public void submitResult(){
        try {
            pollController.updatePoll(poll.getId(), choice);
        } catch (Exception ex) {
            Logger.getLogger(PollBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
