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
import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.ReviewsPK;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 *
 * @author Dan 2016/2/17
 */
@Named
@RequestScoped
public class ReviewsJpaController implements Serializable {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    public ReviewsJpaController() {
    }

    public void create(Reviews reviews) throws PreexistingEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();

            Books books = reviews.getBooks();
            if (books != null) {
                books = em.getReference(books.getClass(), books.getId());
                reviews.setBooks(books);
            }
            Client client1 = reviews.getClient1();
            if (client1 != null) {
                client1 = em.getReference(client1.getClass(), client1.getId());
                reviews.setClient1(client1);
            }
            em.persist(reviews);
            if (books != null) {
                books.getReviewsList().add(reviews);
                books = em.merge(books);
            }
            if (client1 != null) {
                client1.getReviewsList().add(reviews);
                client1 = em.merge(client1);
            }
            // em.flush();
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReviews(reviews.getReviewsPK()) != null) {
                throw new PreexistingEntityException("Reviews " + reviews + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(Reviews reviews) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();

            Reviews persistentReviews = em.find(Reviews.class, reviews.getReviewsPK());
            Books booksOld = persistentReviews.getBooks();
            Books booksNew = reviews.getBooks();
            Client client1Old = persistentReviews.getClient1();
            Client client1New = reviews.getClient1();
            if (booksNew != null) {
                booksNew = em.getReference(booksNew.getClass(), booksNew.getId());
                reviews.setBooks(booksNew);
            }
            if (client1New != null) {
                client1New = em.getReference(client1New.getClass(), client1New.getId());
                reviews.setClient1(client1New);
            }
            reviews = em.merge(reviews);
            if (booksOld != null && !booksOld.equals(booksNew)) {
                booksOld.getReviewsList().remove(reviews);
                booksOld = em.merge(booksOld);
            }
            if (booksNew != null && !booksNew.equals(booksOld)) {
                booksNew.getReviewsList().add(reviews);
                booksNew = em.merge(booksNew);
            }
            if (client1Old != null && !client1Old.equals(client1New)) {
                client1Old.getReviewsList().remove(reviews);
                client1Old = em.merge(client1Old);
            }
            if (client1New != null && !client1New.equals(client1Old)) {
                client1New.getReviewsList().add(reviews);
                client1New = em.merge(client1New);
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
                ReviewsPK id = reviews.getReviewsPK();
                if (findReviews(id) == null) {
                    throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(ReviewsPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();

            Reviews reviews;
            try {
                reviews = em.getReference(Reviews.class, id);
                reviews.getReviewsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.", enfe);
            }
            Books books = reviews.getBooks();
            if (books != null) {
                books.getReviewsList().remove(reviews);
                books = em.merge(books);
            }
            Client client1 = reviews.getClient1();
            if (client1 != null) {
                client1.getReviewsList().remove(reviews);
                client1 = em.merge(client1);
            }
            em.remove(reviews);
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

    public List<Reviews> findReviewsEntities() {
        return findReviewsEntities(true, -1, -1);
    }

    public List<Reviews> findReviewsEntities(int maxResults, int firstResult) {
        return findReviewsEntities(false, maxResults, firstResult);
    }

    private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reviews.class));
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

    public Reviews findReviews(ReviewsPK id) {
        //EntityManager em = getEntityManager();
        try {
            return em.find(Reviews.class, id);
        } finally {
            // em.close();
        }
    }

    public int getReviewsCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reviews> rt = cq.from(Reviews.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            // em.close();
        }
    }

    public List<Reviews> findAllReviews() {
        Query q = em.createQuery("SELECT r FROM Reviews r");
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public List<Reviews> findReviewByBookID(int id) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.reviewsPK.book = :book");
        q.setParameter("book", id);
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public List<Reviews> findReviewByCreationDate(Date date) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.creationDate = :creationDate");
        q.setParameter("creationDate", date);
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public List<Reviews> findReviewByClientID(int clientId) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.reviewsPK.client = :client");
        q.setParameter("client", clientId);
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public List<Reviews> findReviewByRateing(int rating) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.rating = :rating");
        q.setParameter("rating", rating);
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public List<Reviews> findReviewByApprovalStatus(Boolean approvalStatus) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.approval = :approval");
        q.setParameter("approval", approvalStatus);
        List<Reviews> results = (List<Reviews>) q.getResultList();

        return results;
    }

    public Reviews findReviewByPK(ReviewsPK id) {
        Query q = em.createQuery("SELECT r FROM Reviews r WHERE r.reviewsPK.book = :book and r.reviewsPK.client = :client");
        q.setParameter("book", id.getBook());
        q.setParameter("client", id.getClient());
        Reviews result = (Reviews) q.getSingleResult();

        return result;
    }

    public void updateRemovalStatus(ReviewsPK id) throws Exception {
        Reviews review = findReviewByPK(id);
        review.setApproval(true);
        edit(review);
    }

    public boolean reviewExists(ReviewsPK pk) {
        Reviews review = findReviewByPK(pk);
        return review != null;
    }
}
