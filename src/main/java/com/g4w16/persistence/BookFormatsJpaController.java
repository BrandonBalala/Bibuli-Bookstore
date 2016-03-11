/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookFormatsPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Books;
import com.g4w16.entities.Format;
import com.g4w16.interfaces.BookFormatsJpaInterface;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Brandon Balala
 */
@Named
@RequestScoped
public class BookFormatsJpaController implements Serializable, BookFormatsJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Inject
    FormatJpaController formatController;

    @Inject
    BooksJpaController booksController;

    /**
     * Default constructor
     */
    public BookFormatsJpaController() {
    }

    @Override
    public void create(BookFormats bookformats) throws PreexistingEntityException, RollbackFailureException, Exception {

        if (bookformats.getBookFormatsPK() == null) {
            bookformats.setBookFormatsPK(new BookFormatsPK());
        }

        if (!booksController.bookExists(bookformats.getBookFormatsPK().getBook())) {
            throw new NonexistentEntityException("Book does not exist");
        }

        if (!formatController.formatExists(bookformats.getBookFormatsPK().getFormat())) {
            throw new NonexistentEntityException("Format does not exist");
        }

        if (bookFormatExists(bookformats.getBookFormatsPK())) {
            throw new PreexistingEntityException("BookIFormat entry with the given book id and format already exists");
        }

        //bookformats.getBookFormatsPK().setBook(bookformats.getBooks().getId());
        //bookformats.getBookFormatsPK().setFormat(bookformats.getFormat1().getType());
        try {
            utx.begin();
            Books books = bookformats.getBooks();
            if (books != null) {
                books = em.getReference(books.getClass(), books.getId());
                bookformats.setBooks(books);
            }
            Format format1 = bookformats.getFormat1();
            if (format1 != null) {
                format1 = em.getReference(format1.getClass(), format1.getType());
                bookformats.setFormat1(format1);
            }
            em.persist(bookformats);
            if (books != null) {
                books.getBookFormatsList().add(bookformats);
                books = em.merge(books);
            }
            if (format1 != null) {
                format1.getBookFormatsList().add(bookformats);
                format1 = em.merge(format1);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findBookFormatByID(bookformats.getBookFormatsPK()) != null) {
                throw new PreexistingEntityException("BookFormats " + bookformats + " already exists.", ex);
            }
            throw ex;
        }
    }

    @Override
    public void edit(BookFormats bookformats) throws NonexistentEntityException, RollbackFailureException, Exception {
        //bookformats.getBookFormatsPK().setBook(bookformats.getBooks().getId());
        //bookformats.getBookFormatsPK().setFormat(bookformats.getFormat1().getType());
        try {
            utx.begin();
            BookFormats persistentBookFormats = em.find(BookFormats.class, bookformats.getBookFormatsPK());
            Books booksOld = persistentBookFormats.getBooks();
            Books booksNew = bookformats.getBooks();
            Format format1Old = persistentBookFormats.getFormat1();
            Format format1New = bookformats.getFormat1();
            if (booksNew != null) {
                booksNew = em.getReference(booksNew.getClass(), booksNew.getId());
                bookformats.setBooks(booksNew);
            }
            if (format1New != null) {
                format1New = em.getReference(format1New.getClass(), format1New.getType());
                bookformats.setFormat1(format1New);
            }
            bookformats = em.merge(bookformats);
            if (booksOld != null && !booksOld.equals(booksNew)) {
                booksOld.getBookFormatsList().remove(bookformats);
                booksOld = em.merge(booksOld);
            }
            if (booksNew != null && !booksNew.equals(booksOld)) {
                booksNew.getBookFormatsList().add(bookformats);
                booksNew = em.merge(booksNew);
            }
            if (format1Old != null && !format1Old.equals(format1New)) {
                format1Old.getBookFormatsList().remove(bookformats);
                format1Old = em.merge(format1Old);
            }
            if (format1New != null && !format1New.equals(format1Old)) {
                format1New.getBookFormatsList().add(bookformats);
                format1New = em.merge(format1New);
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
                BookFormatsPK id = bookformats.getBookFormatsPK();
                if (findBookFormatByID(id) == null) {
                    throw new NonexistentEntityException("The bookformats with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(BookFormatsPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            System.out.println("1Book id: " + id.getBook());
            System.out.println("1Format: " + id.getFormat());

            utx.begin();
            BookFormats bookformats;
            try {
                bookformats = em.getReference(BookFormats.class, id);
                bookformats.getBookFormatsPK();
                System.out.println("2Book id: " + bookformats.getBookFormatsPK().getBook());
                System.out.println("2Format: " + bookformats.getBookFormatsPK().getFormat());
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookformats with id " + id + " no longer exists.", enfe);
            }
            Books books = bookformats.getBooks();
            System.out.println("3Book id: " + books.getId());
            if (books != null) {
                books.getBookFormatsList().remove(bookformats);
                books = em.merge(books);
            }
            Format format1 = bookformats.getFormat1();
            System.out.println("3Format: " + format1.getType());
            if (format1 != null) {
                format1.getBookFormatsList().remove(bookformats);
                format1 = em.merge(format1);
            }
            em.remove(bookformats);
            System.out.println("Removed");
            utx.commit();
            System.out.println("Comitted");
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
    public List<BookFormats> findAllBookFormats() {
        return findBookFormatsEntities(true, -1, -1);
    }

    @Override
    public List<BookFormats> findBookFormatsEntities(int maxResults, int firstResult) {
        return findBookFormatsEntities(false, maxResults, firstResult);
    }

    private List<BookFormats> findBookFormatsEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BookFormats.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public BookFormats findBookFormatByID(BookFormatsPK id) {
        return em.find(BookFormats.class, id);
    }

    @Override
    public int getBookFormatsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<BookFormats> rt = cq.from(BookFormats.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public boolean bookFormatExists(BookFormatsPK pk) {
        BookFormats result = findBookFormatByID(pk);
        return result != null;
    }

}
