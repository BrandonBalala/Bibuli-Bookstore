/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Poll;
import com.g4w16.interfaces.PollJpaInterface;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Brandon Balala
 */
@Named("pollController")
@RequestScoped
public class PollJpaController implements Serializable, PollJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext//(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public PollJpaController() {
    }

    @Override
    public void create(Poll poll) throws RollbackFailureException, Exception {
        try {
            utx.begin();
            em.persist(poll);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    @Override
    public void edit(Poll poll) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            poll = em.merge(poll);
            utx.commit();

            System.out.println("Poll: " + poll.getFirstCount());
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = poll.getId();
                if (findPollByID(id) == null) {
                    throw new NonexistentEntityException("The poll with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Poll poll;
            try {
                poll = em.getReference(Poll.class, id);
                poll.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The poll with id " + id + " no longer exists.", enfe);
            }
            em.remove(poll);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    @Override
    public List<Poll> findAllPolls() {
        return findPollEntities(true, -1, -1);
    }

    public List<Poll> findPollEntities(int maxResults, int firstResult) {
        return findPollEntities(false, maxResults, firstResult);
    }

    private List<Poll> findPollEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Poll.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Poll findPollByID(Integer id) {
        return em.find(Poll.class, id);
    }

    @Override
    public int getPollCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Poll> rt = cq.from(Poll.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void updatePoll(int pollID, int choice) throws Exception {
        Poll poll = findPollByID(pollID);
        int count;

        switch (choice) {
            case 1:
                count = poll.getFirstCount() + 1;
                poll.setFirstCount(count);
                break;
            case 2:
                count = poll.getSecondCount() + 1;
                poll.setSecondCount(count);
                break;
            case 3:
                count = poll.getThirdCount() + 1;
                poll.setThirdCount(count);
                break;
            case 4:
                count = poll.getFourthCount() + 1;
                poll.setFourthCount(count);
                break;
            default:
                throw new Exception("Invalid poll choice");
        }
        edit(poll);
    }

    public int getAnswerCount(int pollId){
        Poll poll = findPollByID(pollId);
        int totalCount=poll.getFirstCount()+poll.getSecondCount()+poll.getThirdCount()+poll.getFourthCount();
        return totalCount;
    }

    public List<Poll> findSelectedPolls() {
        Query q = em.createQuery("SELECT p FROM Poll p WHERE p.selected=1");
        return (List<Poll>) q.getResultList();

    }
}
