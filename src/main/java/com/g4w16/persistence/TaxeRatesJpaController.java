/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.TaxeRates;
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
public class TaxeRatesJpaController implements Serializable {

	@Resource
    private UserTransaction utx;

	@PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public TaxeRatesJpaController(){
    }

    public void create(TaxeRates taxeRates) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            em.persist(taxeRates);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }

            if (findTaxeRates(taxeRates.getProvince()) != null) {
                throw new PreexistingEntityException("TaxeRates " + taxeRates + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(TaxeRates taxeRates) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            taxeRates = em.merge(taxeRates);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = taxeRates.getProvince();
                if (findTaxeRates(id) == null) {
                    throw new NonexistentEntityException("The taxeRates with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            TaxeRates taxeRates;
            try {
                taxeRates = em.getReference(TaxeRates.class, id);
                taxeRates.getProvince();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taxeRates with id " + id + " no longer exists.", enfe);
            }
            em.remove(taxeRates);
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

    public List<TaxeRates> findTaxeRatesEntities() {
        return findTaxeRatesEntities(true, -1, -1);
    }

    public List<TaxeRates> findTaxeRatesEntities(int maxResults, int firstResult) {
        return findTaxeRatesEntities(false, maxResults, firstResult);
    }

    private List<TaxeRates> findTaxeRatesEntities(boolean all, int maxResults, int firstResult) {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(TaxeRates.class));
		Query q = em.createQuery(cq);
		if (!all) {
			q.setMaxResults(maxResults);
			q.setFirstResult(firstResult);
		}
		return q.getResultList();
    }

    public TaxeRates findTaxeRates(String id) {
		return em.find(TaxeRates.class, id);
    }

    public int getTaxeRatesCount() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<TaxeRates> rt = cq.from(TaxeRates.class);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
    }

    public TaxeRates getTaxeRateByProvince(String province) {
	Query q = em.createQuery("SELECT p FROM Province p WHERE p.id = :province");
        q.setParameter("province",province);
        return (TaxeRates)q.getSingleResult();
    }

}
