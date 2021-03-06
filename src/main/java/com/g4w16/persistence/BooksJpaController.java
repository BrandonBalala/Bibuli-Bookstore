package com.g4w16.persistence;

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
import com.g4w16.entities.BookFormatsPK;
import com.g4w16.entities.BookIdentifiersPK;
import com.g4w16.entities.Books;
import com.g4w16.entities.Format;
import com.g4w16.entities.IdentifierType;
import com.g4w16.interfaces.BooksJpaInterface;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.transaction.UserTransaction;
import javax.inject.Named;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.Expression;

/**
 *
 * @author Brandon Balala
 */
@Named("bookController")
@RequestScoped
public class BooksJpaController implements Serializable, BooksJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Inject
    private BookIdentifiersJpaController bookIdentifierController;

    @Inject
    private ContributorJpaController contributorController;

    @Inject
    private BookFormatsJpaController bookFormatController;

    @Inject
    private GenreJpaController genreController;

    /**
     * Default constructor
     */
    public BooksJpaController() {
    }

    @Override
    public void create(Books book) throws RollbackFailureException, Exception {
        int bookID;

        //validateNewBook(book);

//        List<Genre> tempGenres = book.getGenreList();
//        List<Contributor> tempContributors = book.getContributorList();
//        List<BookFormats> tempBookFormats = book.getBookFormatsList();
//        List<BookIdentifiers> tempBookIdentifiers = book.getBookIdentifiersList();
//
//        book.setGenreList(new ArrayList<Genre>());
//        book.setContributorList(new ArrayList<Contributor>());
//        book.setBookFormatsList(new ArrayList<BookFormats>());
//        book.setBookIdentifiersList(new ArrayList<BookIdentifiers>());
//
//        if (book.getSalesDetailsList() == null) {
//            book.setSalesDetailsList(new ArrayList<SalesDetails>());
//        }
//        if (book.getReviewsList() == null) {
//            book.setReviewsList(new ArrayList<Reviews>());
//        }

        try {
            utx.begin();
            em.persist(book);
            utx.commit();
            bookID = book.getId();
            System.out.println("BOOK ID after commit: " + bookID);
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
//
//        try {
//            //Create the contributors
//            for (Contributor contributor : tempContributors) {//(int cntr = 0; cntr < tempContributors.size(); cntr++){
//                //Contributor contributor = tempContributors.get(cntr);
//
//                int contributorID = contributorController.findContributorIdByNameAndType(contributor.getName(), contributor.getContribution().getType());
//
//                if (contributorID == -1) {
//                    contributorController.create(contributor);
//                }
//
//                Contributor contributorRef = em.getReference(Contributor.class, contributor.getId());
//                book.getContributorList().add(contributorRef);
//            }
//
//            //Create the book formats
//            for (BookFormats bookFormat : tempBookFormats) {
//                BookFormatsPK pk = bookFormat.getBookFormatsPK();
//                pk.setBook(bookID);
//                bookFormat.setBookFormatsPK(pk);
//
//                if (!bookFormatController.bookFormatExists(pk)) {
//                    bookFormatController.create(bookFormat);
//                }
//
//                BookFormats bookFormatRef = em.getReference(BookFormats.class, pk);
//                Format formatRef = em.getReference(Format.class, bookFormatRef.getBookFormatsPK().getFormat());
//                bookFormatRef.setFormat1(formatRef);
//                book.getBookFormatsList().add(bookFormatRef);
//            }
//
//            //Create the book identifiers
//            for (BookIdentifiers bookIdentifier : tempBookIdentifiers) {
//                BookIdentifiersPK pk = bookIdentifier.getBookIdentifiersPK();
//                pk.setBook(bookID);
//                bookIdentifier.setBookIdentifiersPK(pk);
//
//                if (!bookIdentifierController.bookIdentifierExists(pk)) {
//                    bookIdentifierController.create(bookIdentifier);
//                }
//
//                BookIdentifiers bookIdentifierRef = em.getReference(BookIdentifiers.class, pk);
//                IdentifierType identifierTypeRef = em.getReference(IdentifierType.class, bookIdentifierRef.getBookIdentifiersPK().getType());
//                bookIdentifierRef.setIdentifierType(identifierTypeRef);
//                book.getBookIdentifiersList().add(bookIdentifierRef);
//            }
//
//            //Create the genre
//            for (Genre genre : tempGenres) {
//                if (!genreController.genreExists(genre.getType())) {
//                    genreController.create(genre);
//                }
//
//                Genre genreRef = em.getReference(Genre.class, genre.getType());
//                book.getGenreList().add(genreRef);
//            }
//
//            /**
//             * *****************************************
//             */
//            for (Contributor contributorListContributor : book.getContributorList()) {
//                contributorListContributor.getBooksList().add(book);
//                contributorController.edit(contributorListContributor);
//                //contributorListContributor = em.merge(contributorListContributor);
//            }
//            for (Genre genreListGenre : book.getGenreList()) {
//                genreListGenre.getBooksList().add(book);
//                genreController.edit(genreListGenre);
//                //genreListGenre = em.merge(genreListGenre);
//            }
//
//            for (BookIdentifiers bookIdentifiersListBookIdentifiers : book.getBookIdentifiersList()) {
//                bookIdentifiersListBookIdentifiers.setBooks(book);
//                String type = bookIdentifiersListBookIdentifiers.getBookIdentifiersPK().getType();
//                bookIdentifiersListBookIdentifiers.setIdentifierType(new IdentifierType(type));
//                bookIdentifierController.edit(bookIdentifiersListBookIdentifiers);
//            }
//
//            for (BookFormats bookformatsListBookformats : book.getBookFormatsList()) {
//                bookformatsListBookformats.setBooks(book);
//                String type = bookformatsListBookformats.getBookFormatsPK().getFormat();
//                bookformatsListBookformats.setFormat1(new Format(type));
//                bookFormatController.edit(bookformatsListBookformats);
//            }
//        } catch (Exception ex) {
//            destroy(bookID);
//            System.out.println("Deleting book");
//            ex.printStackTrace();
//            throw ex;
//        }
    }

    @Override
    public void edit(Books books) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        
        try {
            utx.begin();
//            Books persistentBooks = em.find(Books.class, books.getId());
//            System.out.println("+++++++++++++++++++"+persistentBooks.toString());
//            List<Contributor> contributorListOld = persistentBooks.getContributorList();
//            List<Contributor> contributorListNew = books.getContributorList();
//            List<Genre> genreListOld = persistentBooks.getGenreList();
//            List<Genre> genreListNew = books.getGenreList();
//            List<BookIdentifiers> bookIdentifiersListOld = persistentBooks.getBookIdentifiersList();
//            List<BookIdentifiers> bookIdentifiersListNew = books.getBookIdentifiersList();
//            List<SalesDetails> salesDetailsListOld = persistentBooks.getSalesDetailsList();
//            List<SalesDetails> salesDetailsListNew = books.getSalesDetailsList();
//            List<Reviews> reviewsListOld = persistentBooks.getReviewsList();
//            List<Reviews> reviewsListNew = books.getReviewsList();
//            List<BookFormats> bookFormatsListOld = persistentBooks.getBookFormatsList();
//            List<BookFormats> bookFormatsListNew = books.getBookFormatsList();
//            List<String> illegalOrphanMessages = null;
//            for (BookIdentifiers bookIdentifiersListOldBookIdentifiers : bookIdentifiersListOld) {
//                if (!bookIdentifiersListNew.contains(bookIdentifiersListOldBookIdentifiers)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain BookIdentifiers " + bookIdentifiersListOldBookIdentifiers + " since its books field is not nullable.");
//                }
//            }
//            System.out.println("###########################BookIdentifiers");
//            for (SalesDetails salesDetailsListOldSalesDetails : salesDetailsListOld) {
//                if (!salesDetailsListNew.contains(salesDetailsListOldSalesDetails)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain SalesDetails " + salesDetailsListOldSalesDetails + " since its book field is not nullable.");
//                }
//            }
//            System.out.println("###########################SalesDetails");
//            for (Reviews reviewsListOldReviews : reviewsListOld) {
//                if (!reviewsListNew.contains(reviewsListOldReviews)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Reviews " + reviewsListOldReviews + " since its books field is not nullable.");
//                }
//            }
//            System.out.println("###########################Reviews");
//            for (BookFormats bookFormatsListOldBookFormats : bookFormatsListOld) {
//                if (!bookFormatsListNew.contains(bookFormatsListOldBookFormats)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain BookFormats " + bookFormatsListOldBookFormats + " since its books field is not nullable.");
//                }
//            }
//            System.out.println("###########################BookFormats");
//            if (illegalOrphanMessages != null) {
//                throw new newpackage.exceptions.IllegalOrphanException(illegalOrphanMessages);
//            }
//            List<Contributor> attachedContributorListNew = new ArrayList<Contributor>();
//            for (Contributor contributorListNewContributorToAttach : contributorListNew) {
//                contributorListNewContributorToAttach = em.getReference(contributorListNewContributorToAttach.getClass(), contributorListNewContributorToAttach.getId());
//                attachedContributorListNew.add(contributorListNewContributorToAttach);
//            }
//            contributorListNew = attachedContributorListNew;
//            books.setContributorList(contributorListNew);
//            List<Genre> attachedGenreListNew = new ArrayList<Genre>();
//            for (Genre genreListNewGenreToAttach : genreListNew) {
//                genreListNewGenreToAttach = em.getReference(genreListNewGenreToAttach.getClass(), genreListNewGenreToAttach.getType());
//                attachedGenreListNew.add(genreListNewGenreToAttach);
//            }
//            genreListNew = attachedGenreListNew;
//            books.setGenreList(genreListNew);
//            List<BookIdentifiers> attachedBookIdentifiersListNew = new ArrayList<BookIdentifiers>();
//            for (BookIdentifiers bookIdentifiersListNewBookIdentifiersToAttach : bookIdentifiersListNew) {
//                bookIdentifiersListNewBookIdentifiersToAttach = em.getReference(bookIdentifiersListNewBookIdentifiersToAttach.getClass(), bookIdentifiersListNewBookIdentifiersToAttach.getBookIdentifiersPK());
//                attachedBookIdentifiersListNew.add(bookIdentifiersListNewBookIdentifiersToAttach);
//            }
//            bookIdentifiersListNew = attachedBookIdentifiersListNew;
//            books.setBookIdentifiersList(bookIdentifiersListNew);
//            List<SalesDetails> attachedSalesDetailsListNew = new ArrayList<SalesDetails>();
//            for (SalesDetails salesDetailsListNewSalesDetailsToAttach : salesDetailsListNew) {
//                salesDetailsListNewSalesDetailsToAttach = em.getReference(salesDetailsListNewSalesDetailsToAttach.getClass(), salesDetailsListNewSalesDetailsToAttach.getId());
//                attachedSalesDetailsListNew.add(salesDetailsListNewSalesDetailsToAttach);
//            }
//            salesDetailsListNew = attachedSalesDetailsListNew;
//            books.setSalesDetailsList(salesDetailsListNew);
//            List<Reviews> attachedReviewsListNew = new ArrayList<Reviews>();
//            for (Reviews reviewsListNewReviewsToAttach : reviewsListNew) {
//                reviewsListNewReviewsToAttach = em.getReference(reviewsListNewReviewsToAttach.getClass(), reviewsListNewReviewsToAttach.getReviewsPK());
//                attachedReviewsListNew.add(reviewsListNewReviewsToAttach);
//            }
//            reviewsListNew = attachedReviewsListNew;
//            books.setReviewsList(reviewsListNew);
//            List<BookFormats> attachedBookFormatsListNew = new ArrayList<BookFormats>();
//            for (BookFormats bookFormatsListNewBookFormatsToAttach : bookFormatsListNew) {
//                bookFormatsListNewBookFormatsToAttach = em.getReference(bookFormatsListNewBookFormatsToAttach.getClass(), bookFormatsListNewBookFormatsToAttach.getBookFormatsPK());
//                attachedBookFormatsListNew.add(bookFormatsListNewBookFormatsToAttach);
//            }
//            bookFormatsListNew = attachedBookFormatsListNew;
//            books.setBookFormatsList(bookFormatsListNew);
//            books = em.merge(books);
//            for (Contributor contributorListOldContributor : contributorListOld) {
//                if (!contributorListNew.contains(contributorListOldContributor)) {
//                    contributorListOldContributor.getBooksList().remove(books);
//                    contributorListOldContributor = em.merge(contributorListOldContributor);
//                }
//            }
//            for (Contributor contributorListNewContributor : contributorListNew) {
//                if (!contributorListOld.contains(contributorListNewContributor)) {
//                    contributorListNewContributor.getBooksList().add(books);
//                    contributorListNewContributor = em.merge(contributorListNewContributor);
//                }
//            }
//            for (Genre genreListOldGenre : genreListOld) {
//                if (!genreListNew.contains(genreListOldGenre)) {
//                    genreListOldGenre.getBooksList().remove(books);
//                    genreListOldGenre = em.merge(genreListOldGenre);
//                }
//            }
//            for (Genre genreListNewGenre : genreListNew) {
//                if (!genreListOld.contains(genreListNewGenre)) {
//                    genreListNewGenre.getBooksList().add(books);
//                    genreListNewGenre = em.merge(genreListNewGenre);
//                }
//            }
//            for (BookIdentifiers bookIdentifiersListNewBookIdentifiers : bookIdentifiersListNew) {
//                if (!bookIdentifiersListOld.contains(bookIdentifiersListNewBookIdentifiers)) {
//                    Books oldBooksOfBookIdentifiersListNewBookIdentifiers = bookIdentifiersListNewBookIdentifiers.getBooks();
//                    bookIdentifiersListNewBookIdentifiers.setBooks(books);
//                    bookIdentifiersListNewBookIdentifiers = em.merge(bookIdentifiersListNewBookIdentifiers);
//                    if (oldBooksOfBookIdentifiersListNewBookIdentifiers != null && !oldBooksOfBookIdentifiersListNewBookIdentifiers.equals(books)) {
//                        oldBooksOfBookIdentifiersListNewBookIdentifiers.getBookIdentifiersList().remove(bookIdentifiersListNewBookIdentifiers);
//                        oldBooksOfBookIdentifiersListNewBookIdentifiers = em.merge(oldBooksOfBookIdentifiersListNewBookIdentifiers);
//                    }
//                }
//            }
//            for (SalesDetails salesDetailsListNewSalesDetails : salesDetailsListNew) {
//                if (!salesDetailsListOld.contains(salesDetailsListNewSalesDetails)) {
//                    Books oldBookOfSalesDetailsListNewSalesDetails = salesDetailsListNewSalesDetails.getBook();
//                    salesDetailsListNewSalesDetails.setBook(books);
//                    salesDetailsListNewSalesDetails = em.merge(salesDetailsListNewSalesDetails);
//                    if (oldBookOfSalesDetailsListNewSalesDetails != null && !oldBookOfSalesDetailsListNewSalesDetails.equals(books)) {
//                        oldBookOfSalesDetailsListNewSalesDetails.getSalesDetailsList().remove(salesDetailsListNewSalesDetails);
//                        oldBookOfSalesDetailsListNewSalesDetails = em.merge(oldBookOfSalesDetailsListNewSalesDetails);
//                    }
//                }
//            }
//            for (Reviews reviewsListNewReviews : reviewsListNew) {
//                if (!reviewsListOld.contains(reviewsListNewReviews)) {
//                    Books oldBooksOfReviewsListNewReviews = reviewsListNewReviews.getBooks();
//                    reviewsListNewReviews.setBooks(books);
//                    reviewsListNewReviews = em.merge(reviewsListNewReviews);
//                    if (oldBooksOfReviewsListNewReviews != null && !oldBooksOfReviewsListNewReviews.equals(books)) {
//                        oldBooksOfReviewsListNewReviews.getReviewsList().remove(reviewsListNewReviews);
//                        oldBooksOfReviewsListNewReviews = em.merge(oldBooksOfReviewsListNewReviews);
//                    }
//                }
//            }
//            for (BookFormats bookFormatsListNewBookFormats : bookFormatsListNew) {
//                if (!bookFormatsListOld.contains(bookFormatsListNewBookFormats)) {
//                    Books oldBooksOfBookFormatsListNewBookFormats = bookFormatsListNewBookFormats.getBooks();
//                    bookFormatsListNewBookFormats.setBooks(books);
//                    bookFormatsListNewBookFormats = em.merge(bookFormatsListNewBookFormats);
//                    if (oldBooksOfBookFormatsListNewBookFormats != null && !oldBooksOfBookFormatsListNewBookFormats.equals(books)) {
//                        oldBooksOfBookFormatsListNewBookFormats.getBookFormatsList().remove(bookFormatsListNewBookFormats);
//                        oldBooksOfBookFormatsListNewBookFormats = em.merge(oldBooksOfBookFormatsListNewBookFormats);
//                    }
//                }
//            }
            em.merge(books);
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
                if (findBookByID(id) == null) {
                    throw new NonexistentEntityException("The books with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            
        }
    }

    @Override
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Books books;
            try {
                books = em.getReference(Books.class, id);
                books.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The books with id " + id + " no longer exists.", enfe);
            }
//            List<String> illegalOrphanMessages = null;
//            List<BookIdentifiers> bookIdentifiersListOrphanCheck = books.getBookIdentifiersList();
//            for (BookIdentifiers bookIdentifiersListOrphanCheckBookIdentifiers : bookIdentifiersListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the BookIdentifiers " + bookIdentifiersListOrphanCheckBookIdentifiers + " in its bookIdentifiersList field has a non-nullable books field.");
//            }
//            List<SalesDetails> salesDetailsListOrphanCheck = books.getSalesDetailsList();
//            for (SalesDetails salesDetailsListOrphanCheckSalesDetails : salesDetailsListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the SalesDetails " + salesDetailsListOrphanCheckSalesDetails + " in its salesDetailsList field has a non-nullable book field.");
//            }
//            List<Reviews> reviewsListOrphanCheck = books.getReviewsList();
//            for (Reviews reviewsListOrphanCheckReviews : reviewsListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Reviews " + reviewsListOrphanCheckReviews + " in its reviewsList field has a non-nullable books field.");
//            }
//            List<BookFormats> bookformatsListOrphanCheck = books.getBookFormatsList();
//            for (BookFormats bookformatsListOrphanCheckBookformats : bookformatsListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Bookformats " + bookformatsListOrphanCheckBookformats + " in its bookformatsList field has a non-nullable books field.");
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }

            List<BookIdentifiers> bookIdentifiersList = books.getBookIdentifiersList();
            for (BookIdentifiers bookIdentifier : bookIdentifiersList) {
                em.remove(bookIdentifier);
            }

            List<BookFormats> bookformatsList = books.getBookFormatsList();
            for (BookFormats bookformat : bookformatsList) {
                em.remove(bookformat);
            }

            List<SalesDetails> salesDetailsList = books.getSalesDetailsList();
            for (SalesDetails salesDetail : salesDetailsList) {
                em.remove(salesDetail);
            }

            List<Reviews> reviewsList = books.getReviewsList();
            for (Reviews review : reviewsList) {
                em.remove(review);
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
        }
    }

    @Override
    public List<Books> findAllBooks() {
        return findBooksEntities(true, -1, -1);
    }

    @Override
    public List<Books> findBooksEntities(int maxResults, int firstResult) {
        return findBooksEntities(false, maxResults, firstResult);
    }

    private List<Books> findBooksEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Books.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Books findBookByID(Integer id) {
        return em.find(Books.class, id);
    }

    @Override
    public int getBooksCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Books> rt = cq.from(Books.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Books> findBooksByTitle(String title) {
        Query q = em.createQuery("SELECT b FROM Books b WHERE UPPER(b.title) LIKE :title");
        q.setParameter("title", "%" + title.toUpperCase() + "%");
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public List<Books> findBooksByPublisher(String publisher) {
        Query q = em.createQuery("SELECT b FROM Books b WHERE UPPER(b.publisher) LIKE :publisher");
        q.setParameter("publisher", "%" + publisher.toUpperCase() + "%");
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public List<Books> findBookByPriceRange(BigDecimal min, BigDecimal max) {
        Query q = em.createQuery("SELECT b FROM Books b WHERE b.listPrice BETWEEN :min AND :max");
        q.setParameter("min", min);
        q.setParameter("max", max);
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public List<Books> findBooksByYear(int year) {
        Query q = em.createQuery("SELECT b FROM Books b WHERE EXTRACT(YEAR from b.pubDate) = :year");
        q.setParameter("year", year);
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public Books findBookByIdentifier(String code) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join identifiers = books.join("bookIdentifiersList");
        cq.select(books).distinct(true);
        cq.where(cb.equal(cb.upper(identifiers.get("code")), "%" + code.toUpperCase() + "%"));
        TypedQuery<Books> query = em.createQuery(cq);

        return (Books) query.getSingleResult();
    }

    @Override
    public List<Books> findBooksByContributorName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join contributors = books.join("contributorList");
        cq.select(books).distinct(true);
        cq.where(cb.like(cb.upper(contributors.get("name")), "%" + name.toUpperCase() + "%"));
        TypedQuery<Books> query = em.createQuery(cq);

        return (List<Books>) query.getResultList();
    }

    public List<Books> findBooksByContributorName(String name, int bookID) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join contributors = books.join("contributorList");
        cq.select(books).distinct(true);
        cq.where(cb.equal(contributors.get("name"),name), cb.notEqual(books.get("id"), bookID));
        TypedQuery<Books> query = em.createQuery(cq);

        return (List<Books>) query.getResultList();
    }

    @Override
    public List<Books> findBooksByFormat(String format) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join bookformats = books.join("bookFormatsList");
        Join formats = bookformats.join("format1");
        cq.select(books).distinct(true);
        cq.where(cb.like(cb.upper(formats.get("type")), "%" + format.toUpperCase() + "%"));
        TypedQuery<Books> query = em.createQuery(cq);

        return (List<Books>) query.getResultList();
    }

    @Override
    public List<Books> findBooksByGenre(String genre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join genres = books.join("genreList");
        cq.select(books).distinct(true);
        cq.where(cb.like(cb.upper(genres.get("type")), "%" + genre.toUpperCase() + "%"));
        TypedQuery<Books> query = em.createQuery(cq);

        return (List<Books>) query.getResultList();
    }

    @Override
    public List<Books> findRecommendedBook(String genre, int bookID) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join genres = books.join("genreList");
        cq.select(books).distinct(true);
        cq.where(cb.equal(genres.get("type"), genre), cb.notEqual(books.get("id"), bookID));
        TypedQuery<Books> query = em.createQuery(cq);

        return (List<Books>) query.getResultList();
    }

    @Override
    public List<Books> findNewestBooks(int amount) {
        Query q = em.createQuery("SELECT b FROM Books b ORDER BY b.pubDate DESC");
        q.setMaxResults(amount);
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public List<Books> findRecentlyAddedBooks(int amount) {
        Query q = em.createQuery("SELECT b FROM Books b ORDER BY b.dateEntered DESC");
        q.setMaxResults(amount);
        List<Books> results = (List<Books>) q.getResultList();

        return results;
    }

    @Override
    public List<Books> findBestSellingBook(int amount) {
//        Query q = em.createNativeQuery("SELECT * FROM Books AS b, SalesDetails AS s WHERE (s.book = b.ID) GROUP BY s.book ORDER BY COUNT(s.book) DESC");
//        q.setMaxResults(amount);
//        List<Books> results = (List<Books>) q.getResultList();
//
//        return results;

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root<Books> books = cq.from(Books.class);
        Join salesDetails = books.join("salesDetailsList");
        Expression<Integer> bookID = salesDetails.get("book");
        Expression<Long> count = cb.count(bookID);

        cq.multiselect(books, count.alias("count"));
        cq.groupBy(bookID);
        cq.orderBy(cb.desc(count));
        TypedQuery<Object[]> query = em.createQuery(cq);
        query.setMaxResults(amount);

        List<Object[]> results = (List<Object[]>) query.getResultList();

        List<Books> bookList = new ArrayList<Books>();
        for (int cntr = 0; cntr < results.size(); cntr++) {
            Object[] obj = results.get(cntr);
            bookList.add((Books) obj[0]);
        }

        return bookList;
    }

//    @Override
//    public List<Books> findBooksByRating(int rating) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//
//        CriteriaQuery cq = cb.createQuery();
//        Root<Books> books = cq.from(Books.class);
//        Join reviews = books.join("reviewsList");
//        cq.select(books).distinct(true);
//        cq.groupBy(books.get("id"));
//        cq.having(cb.greaterThanOrEqualTo(cb.avg(reviews.get("rating")), rating));
//        TypedQuery<Books> query = em.createQuery(cq);
//
//        return (List<Books>) query.getResultList();
//    }
    @Override
    public void updateRemovalStatus(boolean status, int bookID) throws Exception {
//        Query q = em.createQuery("UPDATE Books b SET b.removalStatus = :status WHERE b.id  = :id");
//        q.setParameter("status", status);
//        q.setParameter("id", bookID);
//        int updated = q.executeUpdate();
//
//        return updated;
        Books book = findBookByID(bookID);
        book.setRemovalStatus(status);
        edit(book);
    }

    private void validateNewBook(Books book) throws Exception {
        List<BookIdentifiers> bookIdentifiersList = book.getBookIdentifiersList();

        //Book must have at least one identifier
        if (bookIdentifiersList.isEmpty()) {
            throw new Exception("A book needs at least one identifier");
        }

        //Check that identifer is not already use
        for (int cntr = 0; cntr < bookIdentifiersList.size(); cntr++) {
            String code = bookIdentifiersList.get(cntr).getCode();

            if (checkIdentifierUsed(code)) {
                throw new PreexistingEntityException("The book already exists/Identification code already in use");
            }
        }

        //Check that the book has at least one contributor
        if (book.getContributorList().isEmpty()) {
            throw new Exception("A book needs at least one contributor");
        }

        //Check that the book has at least one contributor
        if (book.getGenreList().isEmpty()) {
            throw new Exception("A book needs at least one genre");
        }

        //Check that the book has at least one format
        if (book.getBookFormatsList().isEmpty()) {
            throw new Exception("A book needs at least one book format");
        }
    }

    private void validateBook(Books book) throws Exception {
        //Book must have at least one identifier
        if (book.getBookIdentifiersList().isEmpty()) {
            throw new Exception("A book needs at least one identifier");
        }

        //Check that the book has at least one contributor
        if (book.getContributorList().isEmpty()) {
            throw new Exception("A book needs at least one contributor");
        }

        //Check that the book has at least one contributor
        if (book.getGenreList().isEmpty()) {
            throw new Exception("A book needs at least one genre");
        }

        //Check that the book has at least one format
        if (book.getBookFormatsList().isEmpty()) {
            throw new Exception("A book needs at least one book format");
        }
    }

    private boolean checkIdentifierUsed(String code) {
        List<BookIdentifiers> bookIdentifiers = bookIdentifierController.findBookIdentifiersByCode(code);

        return !bookIdentifiers.isEmpty();
    }

    @Override
    public boolean bookExists(int id) {
        Books book = findBookByID(id);
        return book != null;
    }
}
