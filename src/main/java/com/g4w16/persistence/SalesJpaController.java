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
import com.g4w16.entities.Client;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author BRANDON-PC
 */
public class SalesJpaController implements Serializable {

    public SalesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sales sales) throws RollbackFailureException, Exception {
        if (sales.getSalesDetailsList() == null) {
            sales.setSalesDetailsList(new ArrayList<SalesDetails>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sales sales) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
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
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sales> findSalesEntities() {
        return findSalesEntities(true, -1, -1);
    }

    public List<Sales> findSalesEntities(int maxResults, int firstResult) {
        return findSalesEntities(false, maxResults, firstResult);
    }

    private List<Sales> findSalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sales.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Sales findSales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sales.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sales> rt = cq.from(Sales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
