/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Banner;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author 1232048
 */
@Named
@RequestScoped
public class BannerJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public BannerJpaController() {
    }

    public void create(Banner banner) throws RollbackFailureException, Exception {
        try {
            utx.begin();
            em.persist(banner);
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

    public void edit(Banner banner) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            banner = em.merge(banner);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = banner.getId();
                if (findBanner(id) == null) {
                    throw new NonexistentEntityException("The banner with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }
    
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Banner banner;
            try {
                banner = em.getReference(Banner.class, id);
                banner.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The banner with id " + id + " no longer exists.", enfe);
            }
            em.remove(banner);
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
    
    public List<Banner> findBannerEntities() {
        return findBannerEntities(true, -1, -1);
    }

    public List<Banner> findBannerEntities(int maxResults, int firstResult) {
        return findBannerEntities(false, maxResults, firstResult);
    }

    private List<Banner> findBannerEntities(boolean all, int maxResults, int firstResult) {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(Banner.class));
		Query q = em.createQuery(cq);
		if (!all) {
			q.setMaxResults(maxResults);
			q.setFirstResult(firstResult);
		}
		return q.getResultList();
    }

    public Banner findBanner(Integer id) {
		return em.find(Banner.class, id);
    }

    public int getBannerCount() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<Banner> rt = cq.from(Banner.class);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
    }
    
}
