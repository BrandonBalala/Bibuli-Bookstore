/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Client;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Dan 2016/2/25
 */
@Named
@RequestScoped
public class BillingAddressJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public BillingAddressJpaController() {
    }

    public void create(BillingAddress billingAddress) throws RollbackFailureException, Exception {

        try {
            utx.begin();
            Client client = billingAddress.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getId());
                billingAddress.setClient(client);
            }
            em.persist(billingAddress);
            if (client != null) {
                client.getBillingAddressList().add(billingAddress);
                client = em.merge(client);
            }
            //em.flush();
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

    public void edit(BillingAddress billingAddress) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            BillingAddress persistentBillingAddress = em.find(BillingAddress.class, billingAddress.getId());
            Client clientOld = persistentBillingAddress.getClient();
            Client clientNew = billingAddress.getClient();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getId());
                billingAddress.setClient(clientNew);
            }
            billingAddress = em.merge(billingAddress);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getBillingAddressList().remove(billingAddress);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getBillingAddressList().add(billingAddress);
                clientNew = em.merge(clientNew);
            }
            //em.flush();
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = billingAddress.getId();
                if (findBillingAddress(id) == null) {
                    throw new NonexistentEntityException("The billingAddress with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            BillingAddress billingAddress;
            try {
                billingAddress = em.getReference(BillingAddress.class, id);
                billingAddress.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The billingAddress with id " + id + " no longer exists.", enfe);
            }
            Client client = billingAddress.getClient();
            if (client != null) {
                client.getBillingAddressList().remove(billingAddress);
                client = em.merge(client);
            }
            em.remove(billingAddress);
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

//    public List<BillingAddress> findBillingAddressEntities() {
//        return findBillingAddressEntities(true, -1, -1);
//    }

    public List<BillingAddress> findBillingAddressEntities(int maxResults, int firstResult) {
        return findBillingAddressEntities(false, maxResults, firstResult);
    }

    private List<BillingAddress> findBillingAddressEntities(boolean all, int maxResults, int firstResult) {
       CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BillingAddress.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public BillingAddress findBillingAddress(Integer id) {
        try {
            return em.find(BillingAddress.class, id);
        } finally {
            em.close();
        }
    }

    public int getBillingAddressCount() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		Root<BillingAddress> rt = cq.from(BillingAddress.class);
		cq.select(em.getCriteriaBuilder().count(rt));
		Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
    }

    public List<BillingAddress> findAllBillingAddress(){
        Query q = em.createQuery("SELECT b FROM BillingAddress b");
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public BillingAddress findBillingAddressById(int id){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.id = :id");
        q.setParameter("id", id);
        BillingAddress results = (BillingAddress)q.getSingleResult();

        return results;
    }

    public List<BillingAddress> findBillingAddressByName(String name){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.name = :name");
        q.setParameter("name", name);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public List<BillingAddress> findBillingAddressByProvince(String province){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.province = :province");
        q.setParameter("province", province);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public List<BillingAddress> findBillingAddressByCity(String city){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.city = :city");
        q.setParameter("city", city);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public List<BillingAddress> findBillingAddressByFirstAddress(String address){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.firstCivicAddress = :firstCivicAddress");
        q.setParameter("firstCivicAddress", address);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public List<BillingAddress> findBillingAddressBySecondCivicAddress(String address){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.secondCivicAddress = :secondCivicAddress");
        q.setParameter("secondCivicAddress", address);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

    public List<BillingAddress> findBillingAddressByPostalCode(String postcode){
        Query q = em.createQuery("SELECT b FROM BillingAddress b WHERE b.postalCode = :postalCode");
        q.setParameter("postalCode", postcode);
        List<BillingAddress> results = (List<BillingAddress>) q.getResultList();

        return results;
    }

}
