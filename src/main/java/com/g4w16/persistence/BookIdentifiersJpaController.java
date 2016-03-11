package com.g4w16.persistence;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookFormatsPK;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.BookIdentifiersPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Books;
import com.g4w16.entities.Genre;
import com.g4w16.entities.IdentifierType;
import com.g4w16.interfaces.BookIdentifiersJpaInterface;
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
public class BookIdentifiersJpaController implements Serializable, BookIdentifiersJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Inject
    IdentifierTypeJpaController identifierTypeController;

    @Inject
    BooksJpaController booksController;

    /**
     * Default constructor
     */
    public BookIdentifiersJpaController() {
    }

    @Override
    public void create(BookIdentifiers bookIdentifiers) throws PreexistingEntityException, RollbackFailureException, Exception {

        if (bookIdentifiers.getBookIdentifiersPK() == null) {
            bookIdentifiers.setBookIdentifiersPK(new BookIdentifiersPK());
        }

        if (!booksController.bookExists(bookIdentifiers.getBookIdentifiersPK().getBook())) {
            throw new NonexistentEntityException("Book does not exist");
        }

        if (!identifierTypeController.identifierTypeExists(bookIdentifiers.getBookIdentifiersPK().getType())) {
            throw new NonexistentEntityException("Identifier type does not exist");
        }

        if(bookIdentifierExists(bookIdentifiers.getBookIdentifiersPK())){
            throw new PreexistingEntityException("BookIdentifier entry with the given book id and identifier already exists");
        }

        //bookIdentifiers.getBookIdentifiersPK().setType(bookIdentifiers.getIdentifierType().getType());
        //bookIdentifiers.getBookIdentifiersPK().setBook(bookIdentifiers.getBooks().getId());
        try {
            utx.begin();
            Books books = bookIdentifiers.getBooks();
            if (books != null) {
                books = em.getReference(books.getClass(), books.getId());
                bookIdentifiers.setBooks(books);
            }
            IdentifierType identifierType = bookIdentifiers.getIdentifierType();
            if (identifierType != null) {
                identifierType = em.getReference(identifierType.getClass(), identifierType.getType());
                bookIdentifiers.setIdentifierType(identifierType);
            }
            em.persist(bookIdentifiers);
            if (books != null) {
                books.getBookIdentifiersList().add(bookIdentifiers);
                books = em.merge(books);
            }
            if (identifierType != null) {
                identifierType.getBookIdentifiersList().add(bookIdentifiers);
                identifierType = em.merge(identifierType);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findBookIdentifierByID(bookIdentifiers.getBookIdentifiersPK()) != null) {
                throw new PreexistingEntityException("BookIdentifiers " + bookIdentifiers + " already exists.", ex);
            }
            throw ex;
        }
    }

    @Override
    public void edit(BookIdentifiers bookIdentifiers) throws NonexistentEntityException, RollbackFailureException, Exception {
        //bookIdentifiers.getBookIdentifiersPK().setType(bookIdentifiers.getIdentifierType().getType());
        //bookIdentifiers.getBookIdentifiersPK().setBook(bookIdentifiers.getBooks().getId());
        try {
            utx.begin();
            BookIdentifiers persistentBookIdentifiers = em.find(BookIdentifiers.class, bookIdentifiers.getBookIdentifiersPK());
            Books booksOld = persistentBookIdentifiers.getBooks();
            Books booksNew = bookIdentifiers.getBooks();
            IdentifierType identifierTypeOld = persistentBookIdentifiers.getIdentifierType();
            IdentifierType identifierTypeNew = bookIdentifiers.getIdentifierType();
            if (booksNew != null) {
                booksNew = em.getReference(booksNew.getClass(), booksNew.getId());
                bookIdentifiers.setBooks(booksNew);
            }
            if (identifierTypeNew != null) {
                identifierTypeNew = em.getReference(identifierTypeNew.getClass(), identifierTypeNew.getType());
                bookIdentifiers.setIdentifierType(identifierTypeNew);
            }
            bookIdentifiers = em.merge(bookIdentifiers);
            if (booksOld != null && !booksOld.equals(booksNew)) {
                booksOld.getBookIdentifiersList().remove(bookIdentifiers);
                booksOld = em.merge(booksOld);
            }
            if (booksNew != null && !booksNew.equals(booksOld)) {
                booksNew.getBookIdentifiersList().add(bookIdentifiers);
                booksNew = em.merge(booksNew);
            }
            if (identifierTypeOld != null && !identifierTypeOld.equals(identifierTypeNew)) {
                identifierTypeOld.getBookIdentifiersList().remove(bookIdentifiers);
                identifierTypeOld = em.merge(identifierTypeOld);
            }
            if (identifierTypeNew != null && !identifierTypeNew.equals(identifierTypeOld)) {
                identifierTypeNew.getBookIdentifiersList().add(bookIdentifiers);
                identifierTypeNew = em.merge(identifierTypeNew);
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
                BookIdentifiersPK id = bookIdentifiers.getBookIdentifiersPK();
                if (findBookIdentifierByID(id) == null) {
                    throw new NonexistentEntityException("The bookIdentifiers with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(BookIdentifiersPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            BookIdentifiers bookIdentifiers;
            try {
                bookIdentifiers = em.getReference(BookIdentifiers.class, id);
                bookIdentifiers.getBookIdentifiersPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookIdentifiers with id " + id + " no longer exists.", enfe);
            }
            Books books = bookIdentifiers.getBooks();
            if (books != null) {
                books.getBookIdentifiersList().remove(bookIdentifiers);
                books = em.merge(books);
            }
            IdentifierType identifierType = bookIdentifiers.getIdentifierType();
            if (identifierType != null) {
                identifierType.getBookIdentifiersList().remove(bookIdentifiers);
                identifierType = em.merge(identifierType);
            }
            em.remove(bookIdentifiers);
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
    public List<BookIdentifiers> findAllBookIdentifiers() {
        return findBookIdentifiersEntities(true, -1, -1);
    }

    @Override
    public List<BookIdentifiers> findBookIdentifiersEntities(int maxResults, int firstResult) {
        return findBookIdentifiersEntities(false, maxResults, firstResult);
    }

    private List<BookIdentifiers> findBookIdentifiersEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BookIdentifiers.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public BookIdentifiers findBookIdentifierByID(BookIdentifiersPK id) {
        return em.find(BookIdentifiers.class, id);
    }

    @Override
    public int getBookIdentifiersCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<BookIdentifiers> rt = cq.from(BookIdentifiers.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<BookIdentifiers> findBookIdentifiersByCode(String code) {
        Query q = em.createQuery("SELECT b FROM BookIdentifiers b WHERE b.code = :code");
        q.setParameter("code", code);
        List<BookIdentifiers> results = (List<BookIdentifiers>) q.getResultList();

        return results;
    }

    @Override
    public boolean bookIdentifierExists(BookIdentifiersPK pk) {
        BookIdentifiers result = findBookIdentifierByID(pk);
        return result != null;
    }
}
