/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Sales;
import com.g4w16.entities.Books;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author BRANDON-PC
 */
@Named
@RequestScoped
public class SalesDetailsJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public SalesDetailsJpaController() {
    }

    public void create(SalesDetails salesDetails) throws RollbackFailureException, Exception {
        try {
            utx.begin();
            Sales sale = salesDetails.getSale();
            if (sale != null) {
                sale = em.getReference(sale.getClass(), sale.getId());
                salesDetails.setSale(sale);
            }
            Books book = salesDetails.getBook();
            if (book != null) {
                book = em.getReference(book.getClass(), book.getId());
                salesDetails.setBook(book);
            }
            em.persist(salesDetails);
            if (sale != null) {
                sale.getSalesDetailsList().add(salesDetails);
                sale = em.merge(sale);
            }
            if (book != null) {
                book.getSalesDetailsList().add(salesDetails);
                book = em.merge(book);
            }
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

    public void edit(SalesDetails salesDetails) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            SalesDetails persistentSalesDetails = em.find(SalesDetails.class, salesDetails.getId());
            Sales saleOld = persistentSalesDetails.getSale();
            Sales saleNew = salesDetails.getSale();
            Books bookOld = persistentSalesDetails.getBook();
            Books bookNew = salesDetails.getBook();
            if (saleNew != null) {
                saleNew = em.getReference(saleNew.getClass(), saleNew.getId());
                salesDetails.setSale(saleNew);
            }
            if (bookNew != null) {
                bookNew = em.getReference(bookNew.getClass(), bookNew.getId());
                salesDetails.setBook(bookNew);
            }
            salesDetails = em.merge(salesDetails);
            if (saleOld != null && !saleOld.equals(saleNew)) {
                saleOld.getSalesDetailsList().remove(salesDetails);
                saleOld = em.merge(saleOld);
            }
            if (saleNew != null && !saleNew.equals(saleOld)) {
                saleNew.getSalesDetailsList().add(salesDetails);
                saleNew = em.merge(saleNew);
            }
            if (bookOld != null && !bookOld.equals(bookNew)) {
                bookOld.getSalesDetailsList().remove(salesDetails);
                bookOld = em.merge(bookOld);
            }
            if (bookNew != null && !bookNew.equals(bookOld)) {
                bookNew.getSalesDetailsList().add(salesDetails);
                bookNew = em.merge(bookNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salesDetails.getId();
                if (findSalesDetails(id) == null) {
                    throw new NonexistentEntityException("The salesDetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            SalesDetails salesDetails;
            try {
                salesDetails = em.getReference(SalesDetails.class, id);
                salesDetails.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salesDetails with id " + id + " no longer exists.", enfe);
            }
            Sales sale = salesDetails.getSale();
            if (sale != null) {
                sale.getSalesDetailsList().remove(salesDetails);
                sale = em.merge(sale);
            }
            Books book = salesDetails.getBook();
            if (book != null) {
                book.getSalesDetailsList().remove(salesDetails);
                book = em.merge(book);
            }
            em.remove(salesDetails);
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

    public List<SalesDetails> findSalesDetailsEntities() {
        return findSalesDetailsEntities(true, -1, -1);
    }

    public List<SalesDetails> findSalesDetailsEntities(int maxResults, int firstResult) {
        return findSalesDetailsEntities(false, maxResults, firstResult);
    }

    private List<SalesDetails> findSalesDetailsEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SalesDetails.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SalesDetails findSalesDetails(Integer id) {
        return em.find(SalesDetails.class, id);
    }
    
    public List<SalesDetails> findSalesDetailsByBookId(int id) {
         Query q = em.createQuery("SELECT s FROM SalesDetails s WHERE s.book.id = :id");
        q.setParameter("id", id);
        return (List<SalesDetails>) q.getResultList();
    }

    public int getSalesDetailsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SalesDetails> rt = cq.from(SalesDetails.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
