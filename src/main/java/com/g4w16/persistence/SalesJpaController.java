/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.BillingAddress;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Client;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 *
 * @author Annie So
 */
@Named
@RequestScoped
public class SalesJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public SalesJpaController() {
    }

    /**
     * A method for creating a new sale.
     *
     * @param sales The sale to add to the database.
     */
    public void create(Sales sales) throws RollbackFailureException, Exception {
        if (sales.getSalesDetailsList() == null) {
            sales.setSalesDetailsList(new ArrayList<SalesDetails>());
        }
        try {
            utx.begin();
            BillingAddress billingAddress = sales.getBillingAddress();
            if (billingAddress != null) {
                billingAddress = em.getReference(billingAddress.getClass(), billingAddress.getId());
                sales.setBillingAddress(billingAddress);
            }
            Client client = sales.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getId());
                sales.setClient(client);
            }
            List<SalesDetails> attachedSalesDetailsList = new ArrayList<SalesDetails>();
            for (SalesDetails salesDetailsListSalesDetailsToAttach : sales.getSalesDetailsList()) {
                salesDetailsListSalesDetailsToAttach = em.getReference(salesDetailsListSalesDetailsToAttach.getClass(), salesDetailsListSalesDetailsToAttach.getId());
                attachedSalesDetailsList.add(salesDetailsListSalesDetailsToAttach);
            }
            sales.setSalesDetailsList(attachedSalesDetailsList);
            em.persist(sales);
            if (billingAddress != null) {
                billingAddress.getSalesCollection().add(sales);
                billingAddress = em.merge(billingAddress);
            }
            if (client != null) {
                client.getSalesList().add(sales);
                client = em.merge(client);
            }
            for (SalesDetails salesDetailsListSalesDetails : sales.getSalesDetailsList()) {
                Sales oldSaleOfSalesDetailsListSalesDetails = salesDetailsListSalesDetails.getSale();
                salesDetailsListSalesDetails.setSale(sales);
                salesDetailsListSalesDetails = em.merge(salesDetailsListSalesDetails);
                if (oldSaleOfSalesDetailsListSalesDetails != null) {
                    oldSaleOfSalesDetailsListSalesDetails.getSalesDetailsList().remove(salesDetailsListSalesDetails);
                    oldSaleOfSalesDetailsListSalesDetails = em.merge(oldSaleOfSalesDetailsListSalesDetails);
                }
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
     * A method for updating a sale in the database.
     *
     * It is private because only certain fields should ever be changed.
     *
     * @param sales The edited sale.
     */
    private void edit(Sales sales) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Sales persistentSales = em.find(Sales.class, sales.getId());

            Client clientOld = persistentSales.getClient();
            Client clientNew = sales.getClient();
            List<SalesDetails> salesDetailsListOld = persistentSales.getSalesDetailsList();
            List<SalesDetails> salesDetailsListNew = sales.getSalesDetailsList();
            List<String> illegalOrphanMessages = null;
            for (SalesDetails salesDetailsListOldSalesDetails : salesDetailsListOld) {
                if (!salesDetailsListNew.contains(salesDetailsListOldSalesDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SalesDetails " + salesDetailsListOldSalesDetails + " since its sale field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getId());
                sales.setClient(clientNew);
            }
            List<SalesDetails> attachedSalesDetailsListNew = new ArrayList<SalesDetails>();
            for (SalesDetails salesDetailsListNewSalesDetailsToAttach : salesDetailsListNew) {
                salesDetailsListNewSalesDetailsToAttach = em.getReference(salesDetailsListNewSalesDetailsToAttach.getClass(), salesDetailsListNewSalesDetailsToAttach.getId());
                attachedSalesDetailsListNew.add(salesDetailsListNewSalesDetailsToAttach);
            }
            salesDetailsListNew = attachedSalesDetailsListNew;
            sales.setSalesDetailsList(salesDetailsListNew);
            sales = em.merge(sales);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getSalesList().remove(sales);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getSalesList().add(sales);
                clientNew = em.merge(clientNew);
            }
            for (SalesDetails salesDetailsListNewSalesDetails : salesDetailsListNew) {
                if (!salesDetailsListOld.contains(salesDetailsListNewSalesDetails)) {
                    Sales oldSaleOfSalesDetailsListNewSalesDetails = salesDetailsListNewSalesDetails.getSale();
                    salesDetailsListNewSalesDetails.setSale(sales);
                    salesDetailsListNewSalesDetails = em.merge(salesDetailsListNewSalesDetails);
                    if (oldSaleOfSalesDetailsListNewSalesDetails != null && !oldSaleOfSalesDetailsListNewSalesDetails.equals(sales)) {
                        oldSaleOfSalesDetailsListNewSalesDetails.getSalesDetailsList().remove(salesDetailsListNewSalesDetails);
                        oldSaleOfSalesDetailsListNewSalesDetails = em.merge(oldSaleOfSalesDetailsListNewSalesDetails);
                    }
                }
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
                Integer id = sales.getId();
                if (findSales(id) == null) {
                    throw new NonexistentEntityException("The sales with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    /**
     * A method for deleting a sale.
     *
     * It is private because we don't delete sales from the database and only
     * mark them as being removed. This method is left here in case that changes
     * in the future.
     *
     * @param id The id of the sale to delete from the database.
     */
    private void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Sales sales;
            try {
                sales = em.getReference(Sales.class, id);
                sales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sales with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SalesDetails> salesDetailsListOrphanCheck = sales.getSalesDetailsList();
            for (SalesDetails salesDetailsListOrphanCheckSalesDetails : salesDetailsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sales (" + sales + ") cannot be destroyed since the SalesDetails " + salesDetailsListOrphanCheckSalesDetails + " in its salesDetailsList field has a non-nullable sale field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Client client = sales.getClient();
            if (client != null) {
                client.getSalesList().remove(sales);
                client = em.merge(client);
            }
            em.remove(sales);
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
     * Gets a list of all sales.
     *
     * @return A list of all the sales.
     */
    public List<Sales> findSalesEntities() {
        return findSalesEntities(true, -1, -1);
    }

    /**
     * Gets a list of a subset of all sales.
     *
     * Used to do pagination.
     *
     * @param maxResults The number of sales to get.
     * @param firstResult The starting point of which sales to get.
     * @return A list of the sales for doing pagination.
     */
    public List<Sales> findSalesEntities(int maxResults, int firstResult) {
        return findSalesEntities(false, maxResults, firstResult);
    }

    private List<Sales> findSalesEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Sales.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    /**
     * Gets a specific sale based on the id.
     *
     * @param id The id of the sale to get.
     * @return The sale that matches the id.
     */
    public Sales findSales(Integer id) {
        return em.find(Sales.class, id);
    }

    /**
     * Gets a count of how many sales are in the database.
     *
     * @return The number of sales in the database.
     */
    public int getSalesCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Sales> rt = cq.from(Sales.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Marks a sale and all its details as removed.
     *
     * @param id
     */
    public void removeSale(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        Sales removedSale = em.find(Sales.class, id);

        if (removedSale != null) {
            removedSale.setRemoved(true);

            //Removes all the sales details as well.
            List<SalesDetails> details = removedSale.getSalesDetailsList();
            for (SalesDetails detail : details) {
                detail.setRemoved(true);
            }

            edit(removedSale);
        }
    }

    /**
     * Gets a list of all sales to a certain client.
     *
     * @param clientId The id of the client.
     * @return A list of sales to a client.
     */
    public List<Sales> findSalesByClientId(int clientId) {
        Query q = em.createQuery("SELECT s FROM Sales s WHERE s.client.id = :clientId");
        q.setParameter("clientId", clientId);
        List<Sales> results = (List<Sales>) q.getResultList();

        return results;
    }

    /**
     * Gets a list of each book sold over a date range, and the total sales,
     * total cost, and total profit of each book.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of information about each book sold over a date range.
     */
    public List<Object[]> getTotalSales(Date startDate, Date endDate) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title, SUM(sd.price) AS totalSales, "
                + "SUM(b.wholesalePrice) AS totalCosts, "
                + "(SUM(sd.price) - SUM(b.wholesalePrice)) AS totalProfit "
                + "FROM Sales s JOIN s.salesDetailsList sd JOIN sd.book b "
                + "WHERE sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "GROUP BY b.id "
                + "ORDER BY s.dateEntered ASC", Object[].class);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }

    /**
     * Gets a list of each book bought over a date range, and the price the book
     * was sold for, the cost of the book, and the profit made from the book for
     * a given client.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @param clientId The id of the client.
     * @return A list of information about the books a client bought over a date
     * range.
     */
    public List<Object[]> getSalesByClient(Date startDate, Date endDate, Integer clientId) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title, sd.price, b.wholesalePrice, "
                + "(sd.price - b.wholesalePrice) AS profit "
                + "FROM Sales s JOIN s.client c JOIN s.salesDetailsList sd JOIN sd.book b "
                + "WHERE c.id = :clientId "
                + "AND sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "ORDER BY s.dateEntered ASC", Object[].class);
        q.setParameter("clientId", clientId);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        //TypedQuery<Object[]> q2 = em.createQuery("SELECT b.id, b.title, sd.price, b.wholesalePrice, (sd.price - b.wholesalePrice) AS profit FROM Sales s JOIN s.client c JOIN s.salesDetailsList sd JOIN sd.book b WHERE c.id = :clientId WHERE sd.removed = false AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) ORDER BY s.dateEntered ASC", Object[].class);
        //TypedQuert<Object[]> q3 = em.createQuery("SELECT b.id, b.title, sd.price, b.wholesalePrice, (sd.proce - b.wholesalePrice) AS profit FROM Sales s JOIN s.client c JOIN s.salesDetailsList sd JOIN sd.book b WHERE c.id = :clientId ")
        
        return results;
    }

    /**
     * Gets a list of each book sold over a date range, and the total sales,
     * total cost, and total profit of each book for a specific contributor.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @param contributorName The name of the contributor.
     * @return A list of information about each book sold from a contributor
     * over a date range.
     */
    public List<Object[]> getSalesByContributor(Date startDate, Date endDate, String contributorName) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title, SUM(sd.price) AS totalSales, "
                + "SUM(b.wholesalePrice) AS totalCosts, "
                + "(SUM(sd.price) - SUM(b.wholesalePrice)) AS totalProfit "
                + "FROM Sales s JOIN s.salesDetailsList sd JOIN sd.book b JOIN b.contributorList c "
                + "WHERE UPPER(c.name) = UPPER(:contributorName) "
                + "AND sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "GROUP BY b.id "
                + "ORDER BY s.dateEntered ASC", Object[].class);

        q.setParameter("contributorName", contributorName);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }

    /**
     * Gets a list of each book sold over a date range, and the total sales,
     * total cost, and total profit of each book for a specific publisher.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @param publisherName The name of the publisher.
     * @return A list of information about each book sold from a publisher over
     * a date range.
     */
    public List<Object[]> getSalesByPublisher(Date startDate, Date endDate, String publisherName) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title, SUM(sd.price) AS totalSales, "
                + "SUM(b.wholesalePrice) AS totalCosts, "
                + "(SUM(sd.price) - SUM(b.wholesalePrice)) AS totalProfit "
                + "FROM Sales s JOIN s.salesDetailsList sd JOIN sd.book b "
                + "WHERE UPPER(b.publisher) = UPPER(:publisherName) "
                + "AND sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "GROUP BY b.id "
                + "ORDER BY s.dateEntered ASC", Object[].class);

        q.setParameter("publisherName", publisherName);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }

    /**
     * Gets a list of each book sold over a date range, and the total sales,
     * total cost, and total profit of each book and orders them starting with
     * the top selling books.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of information about each book sold over a date range
     * ordered by the best sellers.
     */
    public List<Object[]> getTopSellers(Date startDate, Date endDate) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title, SUM(sd.price) AS totalSales, "
                + "SUM(b.wholesalePrice) AS totalCosts, "
                + "(SUM(sd.price) - SUM(b.wholesalePrice)) AS totalProfit "
                + "FROM Sales s JOIN s.salesDetailsList sd JOIN sd.book b "
                + "WHERE sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "GROUP BY b.id "
                + "ORDER BY totalSales DESC", Object[].class);

        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }

    /**
     * Gets a list of clients who have bought at least one book over a date
     * range, the total amount they spent, the total cost of what they bought,
     * and the total profit made from that client. The list is ordered by the
     * value of their total purchases.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of information about the top clients order by their total
     * purchases.
     */
    public List<Object[]> getTopClients(Date startDate, Date endDate) {
        TypedQuery<Object[]> q = em.createQuery("SELECT c.id, c.firstName, c.lastName, SUM(sd.price) AS totalSales, "
                + "SUM(b.wholesalePrice) AS totalCosts, "
                + "(SUM(sd.price) - SUM(b.wholesalePrice)) AS totalProfit "
                + "FROM Sales s JOIN s.salesDetailsList sd JOIN sd.book b JOIN s.client c "
                + "WHERE sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE) "
                + "GROUP BY c.id "
                + "ORDER BY totalSales DESC", Object[].class);

        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }

    /**
     * Gets a list of books that were not sold over a date range.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of books that were not sold over a date range.
     */
    public List<Object[]> getZeroSales(Date startDate, Date endDate) {
        TypedQuery<Object[]> q = em.createQuery("SELECT b.id, b.title FROM Books b "
                + "WHERE b.id NOT IN ("
                + "SELECT DISTINCT sd.book.id "
                + "FROM Sales s JOIN s.salesDetailsList sd "
                + "WHERE sd.removed = false "
                + "AND CAST(s.dateEntered AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)"
                + ")", Object[].class);

        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);

        List<Object[]> results = q.getResultList();

        return results;
    }
}
