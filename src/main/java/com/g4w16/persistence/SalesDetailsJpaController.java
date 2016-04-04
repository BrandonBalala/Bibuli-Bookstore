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
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Annie So
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

    /**
     * A method for creating a new sales detail.
     *
     * @param salesDetails The sales detail to add into the database.
     */
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

    /**
     * A method for updating a sales detail in the database.
     *
     * It is private because the only field that would ever change is changing
     * removed to true. This method is used by the removeSalesDetail method.
     *
     * @param salesDetails The edited sales detail.
     */
    private void edit(SalesDetails salesDetails) throws NonexistentEntityException, RollbackFailureException, Exception {
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

    /**
     * A method for deleting a sales detail.
     *
     * It is private since we don't delete sales details from the database and
     * only mark them as being removed. The method is left here in case that
     * changes in the future.
     *
     * @param id The id of the sales detail to delete from the database.
     */
    private void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
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

    /**
     * Gets a list of all the sales details.
     *
     * @return A list of all the sales details.
     */
    public List<SalesDetails> findSalesDetailsEntities() {
        return findSalesDetailsEntities(true, -1, -1);
    }

    /**
     * Gets a list of a subset of all sales details.
     *
     * Used to do pagination.
     *
     * @param maxResults The number of sales details to get.
     * @param firstResult The starting point of which sales details to get.
     * @return A list of the sales details for doing pagination.
     */
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

    /**
     * Gets a specific sales detail based on the id.
     *
     * @param id The id of the sales detail to get.
     * @return The sales detail that matches the id.
     */
    public SalesDetails findSalesDetails(Integer id) {
        return em.find(SalesDetails.class, id);
    }

    /**
     * Gets a count of how many sales details are in the database.
     *
     * @return The number of sales details in the database.
     */
    public int getSalesDetailsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SalesDetails> rt = cq.from(SalesDetails.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Marks a sales detail as removed.
     *
     * @param id The id of the sales detail to mark as removed.
     */
    public void removeSalesDetail(Integer id) throws RollbackFailureException, Exception {
        SalesDetails removedSalesDetail = em.find(SalesDetails.class, id);
        if (removedSalesDetail != null) {
            removedSalesDetail.setRemoved(true);
            edit(removedSalesDetail);
        }
    }

    /**
     * Returns a boolean value telling you whether or not a client owns a
     * certain book.
     *
     * @param clientId The id of the client to check.
     * @param bookId The id of the book to check.
     * @return A boolean value telling you whether or not the client owns the
     * book.
     */
    public boolean ownsBook(Integer clientId, Integer bookId) {
        Query q = em.createQuery("SELECT sd FROM SalesDetails sd, Sales s "
                + "WHERE sd.sale.id = s.id "
                + "AND s.client.id = :clientId "
                + "AND sd.book.id = :bookId "
                + "AND sd.removed = false");
        q.setParameter("clientId", clientId);
        q.setParameter("bookId", bookId);

        List<SalesDetails> results = (List<SalesDetails>) q.getResultList();

        // If there are no results the client does not own the book.
        if (results.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns a list of all the books owned by a certain client.
     *
     * @param clientId The id of the client.
     * @return A list of books the client owns.
     */
    public List<Books> findBooksOwnedByClientId(Integer clientId) {
        // Only select sales details that have not been removed and have a matching client id.
        Query q = em.createQuery("SELECT sd.book FROM SalesDetails sd, Sales s "
                + "WHERE sd.sale.id = s.id "
                + "AND s.client.id = :clientId "
                + "AND sd.removed = false");
        q.setParameter("clientId", clientId);

        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }
}
