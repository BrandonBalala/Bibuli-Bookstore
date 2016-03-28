/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import com.g4w16.entities.Poll;
import com.g4w16.persistence.PollJpaController;
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
 * @author wangd
 */
@Named("pollsBB")
@RequestScoped
public class AdminPollBackingBean implements Serializable { 
    
    private List<Poll> polls;
    private List<Integer> ids ;
    private int pollId;
    private List<Poll> filteredPolls;
    
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
    
    public List<Poll> getFilteredPolls() {
        return filteredPolls;
    }
 
    public void setFilteredPolls(List<Poll> filteredPolls) {
        this.filteredPolls = filteredPolls;
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
    
    public void changeSurvey(){
        
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"+pollId);
    }
    
}
