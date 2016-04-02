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
import com.g4w16.entities.Genre;
import com.g4w16.interfaces.GenreJpaInterface;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
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
 * @author Brandon Balala
 */
@Named
@RequestScoped
public class GenreJpaController implements Serializable, GenreJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public GenreJpaController() {
    }

    @Override
    public void create(Genre genre) throws PreexistingEntityException, RollbackFailureException, Exception {
        System.out.println("In genre create method");
        if (genreExists(genre.getType())) {
            throw new PreexistingEntityException("Genre " + genre + " already exists.");
        }

        if (genre.getBooksList() == null) {
            genre.setBooksList(new ArrayList<Books>());
        }
        System.out.println("a");
        try {
            utx.begin();
            System.out.println("b");
            List<Books> attachedBooksList = new ArrayList<Books>();
            System.out.println("c");
            for (Books booksListBooksToAttach : genre.getBooksList()) {
                booksListBooksToAttach = em.getReference(booksListBooksToAttach.getClass(), booksListBooksToAttach.getId());
                attachedBooksList.add(booksListBooksToAttach);
            }
            System.out.println("d");
            genre.setBooksList(attachedBooksList);
            System.out.println("e");
            em.persist(genre);
            System.out.println("Genre PERSISTED");
            for (Books booksListBooks : genre.getBooksList()) {
                booksListBooks.getGenreList().add(genre);
                booksListBooks = em.merge(booksListBooks);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGenreByID(genre.getType()) != null) {
                throw new PreexistingEntityException("Genre " + genre + " already exists.", ex);
            }
            throw ex;
        }

        System.out.println("Genre created");
    }

    @Override
    public void edit(Genre genre) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Genre persistentGenre = em.find(Genre.class, genre.getType());
            List<Books> booksListOld = persistentGenre.getBooksList();
            List<Books> booksListNew = genre.getBooksList();
            List<Books> attachedBooksListNew = new ArrayList<Books>();
            for (Books booksListNewBooksToAttach : booksListNew) {
                booksListNewBooksToAttach = em.getReference(booksListNewBooksToAttach.getClass(), booksListNewBooksToAttach.getId());
                attachedBooksListNew.add(booksListNewBooksToAttach);
            }
            booksListNew = attachedBooksListNew;
            genre.setBooksList(booksListNew);
            genre = em.merge(genre);
            for (Books booksListOldBooks : booksListOld) {
                if (!booksListNew.contains(booksListOldBooks)) {
                    booksListOldBooks.getGenreList().remove(genre);
                    booksListOldBooks = em.merge(booksListOldBooks);
                }
            }
            for (Books booksListNewBooks : booksListNew) {
                if (!booksListOld.contains(booksListNewBooks)) {
                    booksListNewBooks.getGenreList().add(genre);
                    booksListNewBooks = em.merge(booksListNewBooks);
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
                String id = genre.getType();
                if (findGenreByID(id) == null) {
                    throw new NonexistentEntityException("The genre with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Genre genre;
            try {
                genre = em.getReference(Genre.class, id);
                genre.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genre with id " + id + " no longer exists.", enfe);
            }
            List<Books> booksList = genre.getBooksList();
            for (Books booksListBooks : booksList) {
                booksListBooks.getGenreList().remove(genre);
                booksListBooks = em.merge(booksListBooks);
            }
            em.remove(genre);
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

    @Override
    public List<Genre> findAllGenres() {
        return findGenreEntities(true, -1, -1);
    }

    @Override
    public List<Genre> findGenreEntities(int maxResults, int firstResult) {
        return findGenreEntities(false, maxResults, firstResult);
    }

    private List<Genre> findGenreEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Genre.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Genre findGenreByID(String id) {

        return em.find(Genre.class, id);

    }

    @Override
    public int getGenreCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Genre> rt = cq.from(Genre.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Genre> findAllUsedGenres() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Books> rt = cq.from(Books.class);
        cq.select(rt.get("genreList")).distinct(true);
        TypedQuery<Genre> query = em.createQuery(cq);

        return (List<Genre>) query.getResultList();
    }

    @Override
    public boolean genreExists(String genre) {
        Genre result = findGenreByID(genre);
        return result != null;
    }
}
