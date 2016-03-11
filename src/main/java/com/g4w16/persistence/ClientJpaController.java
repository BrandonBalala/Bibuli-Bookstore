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
import java.util.ArrayList;
import java.util.List;
import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Client;
import com.g4w16.entities.Reviews;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Dan 2016/2/25
 */
@Named
@RequestScoped
public class ClientJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public ClientJpaController() {
    }

    public void create(Client client) throws RollbackFailureException, Exception {

        if (client.getSalesList() == null) {
            client.setSalesList(new ArrayList<Sales>());
        }
        if (client.getBillingAddressList() == null) {
            client.setBillingAddressList(new ArrayList<BillingAddress>());
        }
        if (client.getReviewsList() == null) {
            client.setReviewsList(new ArrayList<Reviews>());
        }

        try {
            utx.begin();

            List<Sales> attachedSalesList = new ArrayList<Sales>();
            for (Sales salesListSalesToAttach : client.getSalesList()) {
                salesListSalesToAttach = em.getReference(salesListSalesToAttach.getClass(), salesListSalesToAttach.getId());
                attachedSalesList.add(salesListSalesToAttach);
            }
            client.setSalesList(attachedSalesList);
            List<BillingAddress> attachedBillingAddressList = new ArrayList<BillingAddress>();
            for (BillingAddress billingAddressListBillingAddressToAttach : client.getBillingAddressList()) {
                billingAddressListBillingAddressToAttach = em.getReference(billingAddressListBillingAddressToAttach.getClass(), billingAddressListBillingAddressToAttach.getId());
                attachedBillingAddressList.add(billingAddressListBillingAddressToAttach);
            }
            client.setBillingAddressList(attachedBillingAddressList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : client.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getReviewsPK());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            client.setReviewsList(attachedReviewsList);
            em.persist(client);
            for (Sales salesListSales : client.getSalesList()) {
                Client oldClientOfSalesListSales = salesListSales.getClient();
                salesListSales.setClient(client);
                salesListSales = em.merge(salesListSales);
                if (oldClientOfSalesListSales != null) {
                    oldClientOfSalesListSales.getSalesList().remove(salesListSales);
                    oldClientOfSalesListSales = em.merge(oldClientOfSalesListSales);
                }
            }
            for (BillingAddress billingAddressListBillingAddress : client.getBillingAddressList()) {
                Client oldClientOfBillingAddressListBillingAddress = billingAddressListBillingAddress.getClient();
                billingAddressListBillingAddress.setClient(client);
                billingAddressListBillingAddress = em.merge(billingAddressListBillingAddress);
                if (oldClientOfBillingAddressListBillingAddress != null) {
                    oldClientOfBillingAddressListBillingAddress.getBillingAddressList().remove(billingAddressListBillingAddress);
                    oldClientOfBillingAddressListBillingAddress = em.merge(oldClientOfBillingAddressListBillingAddress);
                }
            }
            for (Reviews reviewsListReviews : client.getReviewsList()) {
                Client oldClient1OfReviewsListReviews = reviewsListReviews.getClient1();
                reviewsListReviews.setClient1(client);
                reviewsListReviews = em.merge(reviewsListReviews);
                if (oldClient1OfReviewsListReviews != null) {
                    oldClient1OfReviewsListReviews.getReviewsList().remove(reviewsListReviews);
                    oldClient1OfReviewsListReviews = em.merge(oldClient1OfReviewsListReviews);
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

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {

        try {
            utx.begin();

//            Client persistentClient = em.find(Client.class, client.getId());
//            List<Sales> salesListOld = persistentClient.getSalesList();
//            List<Sales> salesListNew = client.getSalesList();
//            List<BillingAddress> billingAddressListOld = persistentClient.getBillingAddressList();
//            List<BillingAddress> billingAddressListNew = client.getBillingAddressList();
//            List<Reviews> reviewsListOld = persistentClient.getReviewsList();
//            List<Reviews> reviewsListNew = client.getReviewsList();
//            List<String> illegalOrphanMessages = null;
//            for (Sales salesListOldSales : salesListOld) {
//                if (!salesListNew.contains(salesListOldSales)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Sales " + salesListOldSales + " since its client field is not nullable.");
//                }
//            }
//            for (BillingAddress billingAddressListOldBillingAddress : billingAddressListOld) {
//                if (!billingAddressListNew.contains(billingAddressListOldBillingAddress)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain BillingAddress " + billingAddressListOldBillingAddress + " since its client field is not nullable.");
//                }
//            }
//            for (Reviews reviewsListOldReviews : reviewsListOld) {
//                if (!reviewsListNew.contains(reviewsListOldReviews)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its client1 field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
//            List<Sales> attachedSalesListNew = new ArrayList<Sales>();
//            for (Sales salesListNewSalesToAttach : salesListNew) {
//                salesListNewSalesToAttach = em.getReference(salesListNewSalesToAttach.getClass(), salesListNewSalesToAttach.getId());
//                attachedSalesListNew.add(salesListNewSalesToAttach);
//            }
//            salesListNew = attachedSalesListNew;
//            client.setSalesList(salesListNew);
//            List<BillingAddress> attachedBillingAddressListNew = new ArrayList<BillingAddress>();
//            for (BillingAddress billingAddressListNewBillingAddressToAttach : billingAddressListNew) {
//                billingAddressListNewBillingAddressToAttach = em.getReference(billingAddressListNewBillingAddressToAttach.getClass(), billingAddressListNewBillingAddressToAttach.getId());
//                attachedBillingAddressListNew.add(billingAddressListNewBillingAddressToAttach);
//            }
//            billingAddressListNew = attachedBillingAddressListNew;
//            client.setBillingAddressList(billingAddressListNew);
//            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
//            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
//                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getReviewsPK());
//                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
//            }
//            reviewsListNew = attachedReviewsListNew;
//            client.setReviewsList(reviewsListNew);
            client = em.merge(client);
//            for (Sales salesListNewSales : salesListNew) {
//                if (!salesListOld.contains(salesListNewSales)) {
//                    Client oldClientOfSalesListNewSales = salesListNewSales.getClient();
//                    salesListNewSales.setClient(client);
//                    salesListNewSales = em.merge(salesListNewSales);
//                    if (oldClientOfSalesListNewSales != null && !oldClientOfSalesListNewSales.equals(client)) {
//                        oldClientOfSalesListNewSales.getSalesList().remove(salesListNewSales);
//                        oldClientOfSalesListNewSales = em.merge(oldClientOfSalesListNewSales);
//                    }
//                }
//            }
//            for (BillingAddress billingAddressListNewBillingAddress : billingAddressListNew) {
//                if (!billingAddressListOld.contains(billingAddressListNewBillingAddress)) {
//                    Client oldClientOfBillingAddressListNewBillingAddress = billingAddressListNewBillingAddress.getClient();
//                    billingAddressListNewBillingAddress.setClient(client);
//                    billingAddressListNewBillingAddress = em.merge(billingAddressListNewBillingAddress);
//                    if (oldClientOfBillingAddressListNewBillingAddress != null && !oldClientOfBillingAddressListNewBillingAddress.equals(client)) {
//                        oldClientOfBillingAddressListNewBillingAddress.getBillingAddressList().remove(billingAddressListNewBillingAddress);
//                        oldClientOfBillingAddressListNewBillingAddress = em.merge(oldClientOfBillingAddressListNewBillingAddress);
//                    }
//                }
//            }
//            for (Reviews reviewsListNewReviews : reviewsListNew) {
//                if (!reviewsListOld.contains(reviewsListNewReviews)) {
//                    Client oldClient1OfReviewsListNewReviews = reviewsListNewReviews.getClient1();
//                    reviewsListNewReviews.setClient1(client);
//                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
//                    if (oldClient1OfReviewsListNewReviews != null && !oldClient1OfReviewsListNewReviews.equals(client)) {
//                        oldClient1OfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
//                        oldClient1OfReviewsListNewReviews = em.merge(oldClient1OfReviewsListNewReviews);
//                    }
//                }
//            }
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
                Integer id = client.getId();
                if (findClientById(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {

        try {
            utx.begin();
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
//            List<String> illegalOrphanMessages = null;
//            List<Sales> salesListOrphanCheck = client.getSalesList();
//            for (Sales salesListOrphanCheckSales : salesListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Sales " + salesListOrphanCheckSales + " in its salesList field has a non-nullable client field.");
//            }
//            List<BillingAddress> billingAddressListOrphanCheck = client.getBillingAddressList();
//            for (BillingAddress billingAddressListOrphanCheckBillingAddress : billingAddressListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the BillingAddress " + billingAddressListOrphanCheckBillingAddress + " in its billingAddressList field has a non-nullable client field.");
//            }
//            List<Reviews> reviewsListOrphanCheck = client.getReviewsList();
//            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable client1 field.");
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            em.remove(client);
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

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            //em.close();
        }
    }


    public int getClientCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            //em.close();
        }
    }

    public List<Client> findAllClients(){
        Query q = em.createQuery("SELECT c FROM Client c");
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public Client findClientById(int id) {
		Query q = em.createQuery("SELECT c FROM Client c WHERE c.id = :id");
        q.setParameter("id", id);
        Client result=(Client)q.getSingleResult();
        return result;
    }

    public List<Client> findClientByTitle(String title){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.title = :title");
        q.setParameter("title", title);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public List<Client> findClientByFirstName(String fname){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.firstName = :firstName");
        q.setParameter("firstName", fname);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public List<Client> findClientByLastName(String lname){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.lastName = :lastName");
        q.setParameter("lastName", lname);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public List<Client> findClientByEmail(String email){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.email = :email");
        q.setParameter("email", email);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

     public Client findClientByEmailAndPassword(String email,String password){
        try
        {
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.email = :email AND c.password = :password");
        q.setParameter("email", email);
        q.setParameter("password", password);
        return  (Client) q.getSingleResult();
        } catch(NoResultException e) {
        return null;
       }
    }
    public List<Client> findClientByCompanyName(String cname){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.companyName = :companyName");
        q.setParameter("companyName", cname);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public List<Client> findClientByHomePhone(String homephone){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.homePhoneNumber = :homePhoneNumber");
        q.setParameter("homePhoneNumber", homephone);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }

    public List<Client> findClientByCellPhone(String cellphone){
        Query q = em.createQuery("SELECT c FROM Client c WHERE c.cellPhoneNumber = :cellPhoneNumber");
        q.setParameter("cellPhoneNumber", cellphone);
        List<Client> results = (List<Client>) q.getResultList();

        return results;
    }


}
