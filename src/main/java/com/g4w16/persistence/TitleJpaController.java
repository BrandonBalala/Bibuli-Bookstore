/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Title;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
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
 * @author BRANDON-PC
 */
@Named
@RequestScoped
public class TitleJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public TitleJpaController() {
    }

    public void create(Title title) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            em.persist(title);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTitle(title.getId()) != null) {
                throw new PreexistingEntityException("Title " + title + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(Title title) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            title = em.merge(title);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = title.getId();
                if (findTitle(id) == null) {
                    throw new NonexistentEntityException("The title with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Title title;
            try {
                title = em.getReference(Title.class, id);
                title.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The title with id " + id + " no longer exists.", enfe);
            }
            em.remove(title);
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

    public List<Title> findTitleEntities() {
        return findTitleEntities(true, -1, -1);
    }

    public List<Title> findTitleEntities(int maxResults, int firstResult) {
        return findTitleEntities(false, maxResults, firstResult);
    }

    private List<Title> findTitleEntities(boolean all, int maxResults, int firstResult) {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(Title.class));
		Query q = em.createQuery(cq);
		if (!all) {
			q.setMaxResults(maxResults);
			q.setFirstResult(firstResult);
		}
		return q.getResultList();
    }

    public Title findTitle(String id) {
		return em.find(Title.class, id);
    }

    public int getTitleCount() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<Title> rt = cq.from(Title.class);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
    }

}
