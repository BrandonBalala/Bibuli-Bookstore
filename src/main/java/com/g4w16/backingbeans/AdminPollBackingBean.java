/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import com.g4w16.entities.Poll;
import com.g4w16.persistence.PollJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangd
 */
@Named("pollsBB")
@RequestScoped
public class AdminPollBackingBean implements Serializable { 
    
    private List<Poll> polls;
    private List<Integer> ids ;
    private int pollId;
    private List<Poll> filteredPolls;
    private Poll selected;

    
    @Inject
    PollJpaController pollJpaController;
    
    @PostConstruct
    public void init() {
        polls = pollJpaController.findAllPolls();
    }
    
    public List<Poll> getPolls(){
        return polls;
    }
    
    public int getPollCount(){
        return pollJpaController.getPollCount();
    }
    
    public List<Integer> getIds() {
        ids=new ArrayList<>();
         for(int i=0;i<polls.size();i++){
            ids.add(i+1);
         }
         return ids;
    }
    
    public int getPollId() {
        return pollId;
    }
 
    public void setPollId(int pollId) {
        this.pollId = pollId;
    }
    
    public Poll getSelected() {
        return selected;
    }

    public void setSelected(Poll selected) {
        this.selected = selected;
    }
    
    public List<Poll> getFilteredPolls() {
        return filteredPolls;
    }
 
    public void setFilteredPolls(List<Poll> filteredPolls) {
        this.filteredPolls = filteredPolls;
    }
    
    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        pollJpaController.edit((Poll) event.getObject());
    }
     
    public void onRowCancel(RowEditEvent event) {}
    
   
    public void changeStatus(Poll p) throws RollbackFailureException, Exception{
       selected=pollJpaController.findPollByID(p.getId());
       selected.setSelected(p.getSelected());
       pollJpaController.edit(selected);
    }
    
}
