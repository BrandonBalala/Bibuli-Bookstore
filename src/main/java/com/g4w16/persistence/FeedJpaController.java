/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Feed;
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
import com.g4w16.interfaces.FeedJpaInterface;

/**
 *
 * @author BRANDON-PC
 */
@Named
@RequestScoped
public class FeedJpaController implements Serializable, FeedJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public FeedJpaController() {
    }

	@Override
    public void create(Feed feed) throws RollbackFailureException, Exception {
        try {
            utx.begin();
            em.persist(feed);
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
    public void edit(Feed feed) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            feed = em.merge(feed);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = feed.getId();
                if (findFeedByID(id) == null) {
                    throw new NonexistentEntityException("The feed with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

	@Override
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Feed feed;
            try {
                feed = em.getReference(Feed.class, id);
                feed.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The feed with id " + id + " no longer exists.", enfe);
            }
            em.remove(feed);
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
    public List<Feed> findAllFeeds() {
        return findFeedEntities(true, -1, -1);
    }

	@Override
    public List<Feed> findFeedEntities(int maxResults, int firstResult) {
        return findFeedEntities(false, maxResults, firstResult);
    }

    private List<Feed> findFeedEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Feed.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

	@Override
    public Feed findFeedByID(Integer id) {
        return em.find(Feed.class, id);
    }

	@Override
    public int getFeedCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Feed> rt = cq.from(Feed.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

	public Feed findFeedByName(String name){
        Query query = em.createQuery("SELECT f FROM Feed f WHERE f.name = :name");
        query.setParameter("name",name);
        return (Feed) query.getSingleResult();
    }

    public List<Feed> findFeedsByURIPattern(String uri){
        Query query = em.createQuery("SELECT f FROM Feed f WHERE f.uri like :uri");
        query.setParameter("uri","%"+uri+'%');
        return (List<Feed>) query.getResultList();
    }
}

