/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.Poll;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface PollJpaInterface{

    public void create(Poll poll) throws RollbackFailureException, Exception;
    public void edit(Poll poll) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<Poll> findAllPolls();
    public List<Poll> findPollEntities(int maxResults, int firstResult);
    public Poll findPollByID(Integer id);
    public Poll findSelectedPoll();
    public int getPollCount();
    public void updatePoll(int pollID, int choice) throws Exception;

}
