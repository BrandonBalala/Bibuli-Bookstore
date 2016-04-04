/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Contributor;
import java.util.ArrayList;
import java.util.List;
import com.g4w16.entities.Genre;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.SalesDetails;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.BookFormats;
import com.g4w16.entities.Books;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import newpackage.exceptions.IllegalOrphanException;
import newpackage.exceptions.NonexistentEntityException;
import newpackage.exceptions.RollbackFailureException;

/**
 *
 * @author wangdan
 */
public class BooksJpaController implements Serializable {

    public BooksJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Books books) throws RollbackFailureException, Exception {
        if (books.getContributorList() == null) {
            books.setContributorList(new ArrayList<Contributor>());
        }
        if (books.getGenreList() == null) {
            books.setGenreList(new ArrayList<Genre>());
        }
        if (books.getBookIdentifiersList() == null) {
            books.setBookIdentifiersList(new ArrayList<BookIdentifiers>());
        }
        if (books.getSalesDetailsList() == null) {
            books.setSalesDetailsList(new ArrayList<SalesDetails>());
        }
        if (books.getReviewsList() == null) {
            books.setReviewsList(new ArrayList<Reviews>());
        }
        if (books.getBookFormatsList() == null) {
            books.setBookFormatsList(new ArrayList<BookFormats>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Contributor> attachedContributorList = new ArrayList<Contributor>();
            for (Contributor contributorListContributorToAttach : books.getContributorList()) {
                contributorListContributorToAttach = em.getReference(contributorListContributorToAttach.getClass(), contributorListContributorToAttach.getId());
                attachedContributorList.add(contributorListContributorToAttach);
            }
            books.setContributorList(attachedContributorList);
            List<Genre> attachedGenreList = new ArrayList<Genre>();
            for (Genre genreListGenreToAttach : books.getGenreList()) {
                genreListGenreToAttach = em.getReference(genreListGenreToAttach.getClass(), genreListGenreToAttach.getType());
                attachedGenreList.add(genreListGenreToAttach);
            }
            books.setGenreList(attachedGenreList);
            List<BookIdentifiers> attachedBookIdentifiersList = new ArrayList<BookIdentifiers>();
            for (BookIdentifiers bookIdentifiersListBookIdentifiersToAttach : books.getBookIdentifiersList()) {
                bookIdentifiersListBookIdentifiersToAttach = em.getReference(bookIdentifiersListBookIdentifiersToAttach.getClass(), bookIdentifiersListBookIdentifiersToAttach.getBookIdentifiersPK());
                attachedBookIdentifiersList.add(bookIdentifiersListBookIdentifiersToAttach);
            }
            books.setBookIdentifiersList(attachedBookIdentifiersList);
            List<SalesDetails> attachedSalesDetailsList = new ArrayList<SalesDetails>();
            for (SalesDetails salesDetailsListSalesDetailsToAttach : books.getSalesDetailsList()) {
                salesDetailsListSalesDetailsToAttach = em.getReference(salesDetailsListSalesDetailsToAttach.getClass(), salesDetailsListSalesDetailsToAttach.getId());
                attachedSalesDetailsList.add(salesDetailsListSalesDetailsToAttach);
            }
            books.setSalesDetailsList(attachedSalesDetailsList);
            List<Reviews> attachedReviewsList = new ArrayList<Reviews>();
            for (Reviews reviewsListReviewsToAttach : books.getReviewsList()) {
                reviewsListReviewsToAttach = em.getReference(reviewsListReviewsToAttach.getClass(), reviewsListReviewsToAttach.getReviewsPK());
                attachedReviewsList.add(reviewsListReviewsToAttach);
            }
            books.setReviewsList(attachedReviewsList);
            List<BookFormats> attachedBookFormatsList = new ArrayList<BookFormats>();
            for (BookFormats bookFormatsListBookFormatsToAttach : books.getBookFormatsList()) {
                bookFormatsListBookFormatsToAttach = em.getReference(bookFormatsListBookFormatsToAttach.getClass(), bookFormatsListBookFormatsToAttach.getBookFormatsPK());
                attachedBookFormatsList.add(bookFormatsListBookFormatsToAttach);
            }
            books.setBookFormatsList(attachedBookFormatsList);
            em.persist(books);
            for (Contributor contributorListContributor : books.getContributorList()) {
                contributorListContributor.getBooksList().add(books);
                contributorListContributor = em.merge(contributorListContributor);
            }
            for (Genre genreListGenre : books.getGenreList()) {
                genreListGenre.getBooksList().add(books);
                genreListGenre = em.merge(genreListGenre);
            }
            for (BookIdentifiers bookIdentifiersListBookIdentifiers : books.getBookIdentifiersList()) {
                Books oldBooksOfBookIdentifiersListBookIdentifiers = bookIdentifiersListBookIdentifiers.getBooks();
                bookIdentifiersListBookIdentifiers.setBooks(books);
                bookIdentifiersListBookIdentifiers = em.merge(bookIdentifiersListBookIdentifiers);
                if (oldBooksOfBookIdentifiersListBookIdentifiers != null) {
                    oldBooksOfBookIdentifiersListBookIdentifiers.getBookIdentifiersList().remove(bookIdentifiersListBookIdentifiers);
                    oldBooksOfBookIdentifiersListBookIdentifiers = em.merge(oldBooksOfBookIdentifiersListBookIdentifiers);
                }
            }
            for (SalesDetails salesDetailsListSalesDetails : books.getSalesDetailsList()) {
                Books oldBookOfSalesDetailsListSalesDetails = salesDetailsListSalesDetails.getBook();
                salesDetailsListSalesDetails.setBook(books);
                salesDetailsListSalesDetails = em.merge(salesDetailsListSalesDetails);
                if (oldBookOfSalesDetailsListSalesDetails != null) {
                    oldBookOfSalesDetailsListSalesDetails.getSalesDetailsList().remove(salesDetailsListSalesDetails);
                    oldBookOfSalesDetailsListSalesDetails = em.merge(oldBookOfSalesDetailsListSalesDetails);
                }
            }
            for (Reviews reviewsListReviews : books.getReviewsList()) {
                Books oldBooksOfReviewsListReviews = reviewsListReviews.getBooks();
                reviewsListReviews.setBooks(books);
                reviewsListReviews = em.merge(reviewsListReviews);
                if (oldBooksOfReviewsListReviews != null) {
                    oldBooksOfReviewsListReviews.getReviewsList().remove(reviewsListReviews);
                    oldBooksOfReviewsListReviews = em.merge(oldBooksOfReviewsListReviews);
                }
            }
            for (BookFormats bookFormatsListBookFormats : books.getBookFormatsList()) {
                Books oldBooksOfBookFormatsListBookFormats = bookFormatsListBookFormats.getBooks();
                bookFormatsListBookFormats.setBooks(books);
                bookFormatsListBookFormats = em.merge(bookFormatsListBookFormats);
                if (oldBooksOfBookFormatsListBookFormats != null) {
                    oldBooksOfBookFormatsListBookFormats.getBookFormatsList().remove(bookFormatsListBookFormats);
                    oldBooksOfBookFormatsListBookFormats = em.merge(oldBooksOfBookFormatsListBookFormats);
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

    public void edit(Books books) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            //em = getEntityManager();
            Books persistentBooks = em.find(Books.class, books.getId());
            List<Contributor> contributorListOld = persistentBooks.getContributorList();
            List<Contributor> contributorListNew = books.getContributorList();
            List<Genre> genreListOld = persistentBooks.getGenreList();
            List<Genre> genreListNew = books.getGenreList();
            List<BookIdentifiers> bookIdentifiersListOld = persistentBooks.getBookIdentifiersList();
            List<BookIdentifiers> bookIdentifiersListNew = books.getBookIdentifiersList();
            List<SalesDetails> salesDetailsListOld = persistentBooks.getSalesDetailsList();
            List<SalesDetails> salesDetailsListNew = books.getSalesDetailsList();
            List<Reviews> reviewsListOld = persistentBooks.getReviewsList();
            List<Reviews> reviewsListNew = books.getReviewsList();
            List<BookFormats> bookFormatsListOld = persistentBooks.getBookFormatsList();
            List<BookFormats> bookFormatsListNew = books.getBookFormatsList();
            List<String> illegalOrphanMessages = null;
            for (BookIdentifiers bookIdentifiersListOldBookIdentifiers : bookIdentifiersListOld) {
                if (!bookIdentifiersListNew.contains(bookIdentifiersListOldBookIdentifiers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookIdentifiers " + bookIdentifiersListOldBookIdentifiers + " since its books field is not nullable.");
                }
            }
            for (SalesDetails salesDetailsListOldSalesDetails : salesDetailsListOld) {
                if (!salesDetailsListNew.contains(salesDetailsListOldSalesDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SalesDetails " + salesDetailsListOldSalesDetails + " since its book field is not nullable.");
                }
            }
            for (Reviews reviewsListOldReviews : reviewsListOld) {
                if (!reviewsListNew.contains(reviewsListOldReviews)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its books field is not nullable.");
                }
            }
            for (BookFormats bookFormatsListOldBookFormats : bookFormatsListOld) {
                if (!bookFormatsListNew.contains(bookFormatsListOldBookFormats)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookFormats " + bookFormatsListOldBookFormats + " since its books field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contributor> attachedContributorListNew = new ArrayList<Contributor>();
            for (Contributor contributorListNewContributorToAttach : contributorListNew) {
                contributorListNewContributorToAttach = em.getReference(contributorListNewContributorToAttach.getClass(), contributorListNewContributorToAttach.getId());
                attachedContributorListNew.add(contributorListNewContributorToAttach);
            }
            contributorListNew = attachedContributorListNew;
            books.setContributorList(contributorListNew);
            List<Genre> attachedGenreListNew = new ArrayList<Genre>();
            for (Genre genreListNewGenreToAttach : genreListNew) {
                genreListNewGenreToAttach = em.getReference(genreListNewGenreToAttach.getClass(), genreListNewGenreToAttach.getType());
                attachedGenreListNew.add(genreListNewGenreToAttach);
            }
            genreListNew = attachedGenreListNew;
            books.setGenreList(genreListNew);
            List<BookIdentifiers> attachedBookIdentifiersListNew = new ArrayList<BookIdentifiers>();
            for (BookIdentifiers bookIdentifiersListNewBookIdentifiersToAttach : bookIdentifiersListNew) {
                bookIdentifiersListNewBookIdentifiersToAttach = em.getReference(bookIdentifiersListNewBookIdentifiersToAttach.getClass(), bookIdentifiersListNewBookIdentifiersToAttach.getBookIdentifiersPK());
                attachedBookIdentifiersListNew.add(bookIdentifiersListNewBookIdentifiersToAttach);
            }
            bookIdentifiersListNew = attachedBookIdentifiersListNew;
            books.setBookIdentifiersList(bookIdentifiersListNew);
            List<SalesDetails> attachedSalesDetailsListNew = new ArrayList<SalesDetails>();
            for (SalesDetails salesDetailsListNewSalesDetailsToAttach : salesDetailsListNew) {
                salesDetailsListNewSalesDetailsToAttach = em.getReference(salesDetailsListNewSalesDetailsToAttach.getClass(), salesDetailsListNewSalesDetailsToAttach.getId());
                attachedSalesDetailsListNew.add(salesDetailsListNewSalesDetailsToAttach);
            }
            salesDetailsListNew = attachedSalesDetailsListNew;
            books.setSalesDetailsList(salesDetailsListNew);
            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getReviewsPK());
                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
            }
            reviewsListNew = attachedReviewsListNew;
            books.setReviewsList(reviewsListNew);
            List<BookFormats> attachedBookFormatsListNew = new ArrayList<BookFormats>();
            for (BookFormats bookFormatsListNewBookFormatsToAttach : bookFormatsListNew) {
                bookFormatsListNewBookFormatsToAttach = em.getReference(bookFormatsListNewBookFormatsToAttach.getClass(), bookFormatsListNewBookFormatsToAttach.getBookFormatsPK());
                attachedBookFormatsListNew.add(bookFormatsListNewBookFormatsToAttach);
            }
            bookFormatsListNew = attachedBookFormatsListNew;
            books.setBookFormatsList(bookFormatsListNew);
            books = em.merge(books);
            for (Contributor contributorListOldContributor : contributorListOld) {
                if (!contributorListNew.contains(contributorListOldContributor)) {
                    contributorListOldContributor.getBooksList().remove(books);
                    contributorListOldContributor = em.merge(contributorListOldContributor);
                }
            }
            for (Contributor contributorListNewContributor : contributorListNew) {
                if (!contributorListOld.contains(contributorListNewContributor)) {
                    contributorListNewContributor.getBooksList().add(books);
                    contributorListNewContributor = em.merge(contributorListNewContributor);
                }
            }
            for (Genre genreListOldGenre : genreListOld) {
                if (!genreListNew.contains(genreListOldGenre)) {
                    genreListOldGenre.getBooksList().remove(books);
                    genreListOldGenre = em.merge(genreListOldGenre);
                }
            }
            for (Genre genreListNewGenre : genreListNew) {
                if (!genreListOld.contains(genreListNewGenre)) {
                    genreListNewGenre.getBooksList().add(books);
                    genreListNewGenre = em.merge(genreListNewGenre);
                }
            }
            for (BookIdentifiers bookIdentifiersListNewBookIdentifiers : bookIdentifiersListNew) {
                if (!bookIdentifiersListOld.contains(bookIdentifiersListNewBookIdentifiers)) {
                    Books oldBooksOfBookIdentifiersListNewBookIdentifiers = bookIdentifiersListNewBookIdentifiers.getBooks();
                    bookIdentifiersListNewBookIdentifiers.setBooks(books);
                    bookIdentifiersListNewBookIdentifiers = em.merge(bookIdentifiersListNewBookIdentifiers);
                    if (oldBooksOfBookIdentifiersListNewBookIdentifiers != null && !oldBooksOfBookIdentifiersListNewBookIdentifiers.equals(books)) {
                        oldBooksOfBookIdentifiersListNewBookIdentifiers.getBookIdentifiersList().remove(bookIdentifiersListNewBookIdentifiers);
                        oldBooksOfBookIdentifiersListNewBookIdentifiers = em.merge(oldBooksOfBookIdentifiersListNewBookIdentifiers);
                    }
                }
            }
            for (SalesDetails salesDetailsListNewSalesDetails : salesDetailsListNew) {
                if (!salesDetailsListOld.contains(salesDetailsListNewSalesDetails)) {
                    Books oldBookOfSalesDetailsListNewSalesDetails = salesDetailsListNewSalesDetails.getBook();
                    salesDetailsListNewSalesDetails.setBook(books);
                    salesDetailsListNewSalesDetails = em.merge(salesDetailsListNewSalesDetails);
                    if (oldBookOfSalesDetailsListNewSalesDetails != null && !oldBookOfSalesDetailsListNewSalesDetails.equals(books)) {
                        oldBookOfSalesDetailsListNewSalesDetails.getSalesDetailsList().remove(salesDetailsListNewSalesDetails);
                        oldBookOfSalesDetailsListNewSalesDetails = em.merge(oldBookOfSalesDetailsListNewSalesDetails);
                    }
                }
            }
            for (Reviews reviewsListNewReviews : reviewsListNew) {
                if (!reviewsListOld.contains(reviewsListNewReviews)) {
                    Books oldBooksOfReviewsListNewReviews = reviewsListNewReviews.getBooks();
                    reviewsListNewReviews.setBooks(books);
                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
                    if (oldBooksOfReviewsListNewReviews != null && !oldBooksOfReviewsListNewReviews.equals(books)) {
                        oldBooksOfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
                        oldBooksOfReviewsListNewReviews = em.merge(oldBooksOfReviewsListNewReviews);
                    }
                }
            }
            for (BookFormats bookFormatsListNewBookFormats : bookFormatsListNew) {
                if (!bookFormatsListOld.contains(bookFormatsListNewBookFormats)) {
                    Books oldBooksOfBookFormatsListNewBookFormats = bookFormatsListNewBookFormats.getBooks();
                    bookFormatsListNewBookFormats.setBooks(books);
                    bookFormatsListNewBookFormats = em.merge(bookFormatsListNewBookFormats);
                    if (oldBooksOfBookFormatsListNewBookFormats != null && !oldBooksOfBookFormatsListNewBookFormats.equals(books)) {
                        oldBooksOfBookFormatsListNewBookFormats.getBookFormatsList().remove(bookFormatsListNewBookFormats);
                        oldBooksOfBookFormatsListNewBookFormats = em.merge(oldBooksOfBookFormatsListNewBookFormats);
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
                Integer id = books.getId();
                if (findBooks(id) == null) {
                    throw new NonexistentEntityException("The books with id " + id + " no longer exists.");
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
            Books books;
            try {
                books = em.getReference(Books.class, id);
                books.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The books with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BookIdentifiers> bookIdentifiersListOrphanCheck = books.getBookIdentifiersList();
            for (BookIdentifiers bookIdentifiersListOrphanCheckBookIdentifiers : bookIdentifiersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the BookIdentifiers " + bookIdentifiersListOrphanCheckBookIdentifiers + " in its bookIdentifiersList field has a non-nullable books field.");
            }
            List<SalesDetails> salesDetailsListOrphanCheck = books.getSalesDetailsList();
            for (SalesDetails salesDetailsListOrphanCheckSalesDetails : salesDetailsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the SalesDetails " + salesDetailsListOrphanCheckSalesDetails + " in its salesDetailsList field has a non-nullable book field.");
            }
            List<Reviews> reviewsListOrphanCheck = books.getReviewsList();
            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable books field.");
            }
            List<BookFormats> bookFormatsListOrphanCheck = books.getBookFormatsList();
            for (BookFormats bookFormatsListOrphanCheckBookFormats : bookFormatsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the BookFormats " + bookFormatsListOrphanCheckBookFormats + " in its bookFormatsList field has a non-nullable books field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contributor> contributorList = books.getContributorList();
            for (Contributor contributorListContributor : contributorList) {
                contributorListContributor.getBooksList().remove(books);
                contributorListContributor = em.merge(contributorListContributor);
            }
            List<Genre> genreList = books.getGenreList();
            for (Genre genreListGenre : genreList) {
                genreListGenre.getBooksList().remove(books);
                genreListGenre = em.merge(genreListGenre);
            }
            em.remove(books);
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

    public List<Books> findBooksEntities() {
        return findBooksEntities(true, -1, -1);
    }

    public List<Books> findBooksEntities(int maxResults, int firstResult) {
        return findBooksEntities(false, maxResults, firstResult);
    }

    private List<Books> findBooksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Books.class));
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

    public Books findBooks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Books.class, id);
        } finally {
            em.close();
        }
    }

    public int getBooksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Books> rt = cq.from(Books.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
