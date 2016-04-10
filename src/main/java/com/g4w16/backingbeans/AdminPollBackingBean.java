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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangd
 * @author Annie So
 */
@Named("pollsBB")
@RequestScoped
public class AdminPollBackingBean implements Serializable {

    private List<Poll> polls;
    private List<Integer> ids;
    private int pollId;
    private List<Poll> filteredPolls;
    private Poll selected;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Inject
    PollJpaController pollJpaController;

    @PostConstruct
    public void init() {
        polls = pollJpaController.findAllPolls();
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public int getPollCount() {
        return pollJpaController.getPollCount();
    }

    public List<Integer> getIds() {
        ids = new ArrayList<>();
        for (int i = 0; i < polls.size(); i++) {
            ids.add(i + 1);
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        pollJpaController.edit((Poll) event.getObject());
    }

    public void onRowCancel(RowEditEvent event) {
    }

    /**
     * Change selected status
     * @param p
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void changeStatus(Poll p) throws RollbackFailureException, Exception {
        selected = pollJpaController.findPollByID(p.getId());
        Poll current;
        if (!p.getSelected()) {
            for (int i = 0; i < polls.size(); i++) {
                current = polls.get(i);
                if (current.getSelected()) {
                    current.setSelected(false);
                    pollJpaController.edit(current);
                    break;
                }
            }

            selected.setSelected(true);
            pollJpaController.edit(selected);
            init();
        }

    }
    
    /**
     * Add a new record with validation for length
     * @param question
     * @param option1
     * @param option2
     * @param option3
     * @param option4 
     */
    public void addAction(String question, String option1, String option2, String option3, String option4) {
        boolean noErrors = true;

        if (question.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Question cannot be longer than 128 characters", "Question cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (option1.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Option 1 cannot be longer than 128 characters", "Option 1 cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (option2.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Option 2 cannot be longer than 128 characters", "Option 2 cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (option3.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Option 3 cannot be longer than 128 characters", "Option 3 cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (option4.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Option 4 cannot be longer than 128 characters", "Option 4 cannot be longer than 128 characters"));
            noErrors = false;
        }
        if (noErrors) {
            try {
                Poll p = new Poll();
                p.setQuestion(question);
                p.setFirstAnswer(option1);
                p.setSecondAnswer(option2);
                p.setThirdAnswer(option3);
                p.setFourthAnswer(option4);
                p.setFirstCount(0);
                p.setSecondCount(0);
                p.setThirdCount(0);
                p.setFourthCount(0);
                p.setSelected(false);
                pollJpaController.create(p);
                init();
                question = "";
                option1 = "";
                option2 = "";
                option3 = "";
                option4 = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception ex) {
                question = "";
                option1 = "";
                option2 = "";
                option3 = "";
                option4 = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
            }
        }
    }
}
