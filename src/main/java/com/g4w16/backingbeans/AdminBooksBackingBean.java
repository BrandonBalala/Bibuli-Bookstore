/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.Books;
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.entities.IdentifierType;
import com.g4w16.persistence.BookFormatsJpaController;
import com.g4w16.persistence.BookIdentifiersJpaController;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.ContributionTypeJpaController;
import com.g4w16.persistence.ContributorJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import com.g4w16.persistence.IdentifierTypeJpaController;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.SalesDetailsJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author wangd
 */
@Named("booksBB")
@SessionScoped
public class AdminBooksBackingBean implements Serializable {

    private List<Books> books;
    private List<Books> saleBooks;
    private List<Books> filteredBooks;
    private Books selectedBook;
    private List<BookFormats> allBookFormats;

    public List<BookFormats> getAllBookFormats() {
        return allBookFormats;
    }

    public void setAllBookFormats(List<BookFormats> allBookFormats) {
        this.allBookFormats = allBookFormats;
    }
    private List<String> status;
    private List<Format> allFormats;
    private List<ContributionType> allContributionType;
    private List<Genre> allGenre;
    private List<Contributor> allAuthors;
    private List<IdentifierType> allIdentifierTypes;
    private List<BookIdentifiers> allBookIdentifiers;

    public List<BookIdentifiers> getAllBookIdentifiers() {
        return allBookIdentifiers;
    }

    public void setAllBookIdentifiers(List<BookIdentifiers> allBookIdentifiers) {
        this.allBookIdentifiers = allBookIdentifiers;
    }

    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Inject
    BooksJpaController booksJpaController;

    @Inject
    GenreJpaController genreJpaController;

    @Inject
    FormatJpaController formatJpaController;

    @Inject
    ContributionTypeJpaController contributionTypeJpaController;

    @Inject
    ContributorJpaController contributorJpaController;

    @Inject
    IdentifierTypeJpaController identifierTypeJpaController;

    @Inject
    ReviewsJpaController reviewsJpaController;

    @Inject
    SalesDetailsJpaController salesDetailsJpaController;

    @Inject
    BookIdentifiersJpaController bookIdentifiersJpaController;

    @Inject
    BookFormatsJpaController bookFormatsJpaController;

    /**
     * For Inventory page
     */
    @PostConstruct
    public void init() {
        books = booksJpaController.findAllBooks();
        BigDecimal min = new BigDecimal(0);
        BigDecimal max = new BigDecimal(999999999);
        saleBooks = booksJpaController.findBookByPriceRange(min, max);
        allFormats = formatJpaController.findAllFormats();
        allContributionType = contributionTypeJpaController.findAllContributionTypes();
        allGenre = genreJpaController.findAllGenres();
        allAuthors = contributorJpaController.findAllContributors();
        allIdentifierTypes = identifierTypeJpaController.findAllIdentifierTypes();
        allBookFormats = bookFormatsJpaController.findAllBookFormats();
        allBookIdentifiers = bookIdentifiersJpaController.findAllBookIdentifiers();
    }

    /**
     * ****************All books**********************
     */
    public List<Books> getBooks() {
        return books;
    }

    public List<Books> getSaleBooks() {
        return saleBooks;
    }

    public int getSalesCount() {
        return saleBooks.size();
    }

    public int getBooksCount() {
        return booksJpaController.getBooksCount();
    }

    public List<String> getStatus() {
        status = new ArrayList<>(Arrays.asList("TRUE", "FALSE"));
        return status;
    }

    public List<Books> getFilteredBooks() {
        return filteredBooks;
    }

    public void setFilteredClients(List<Books> filteredBooks) {
        this.filteredBooks = filteredBooks;
    }

    public Books getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Books selectedBook) {
        this.selectedBook = selectedBook;
    }

    public String showDetail(Books book) {
        selectedBook = booksJpaController.findBookByID(book.getId());
        return "admin_edit_book";
    }

    public String updateBook() throws Exception {
        System.out.println(">>>>>>>>>>>>.update");

        Books originalBook = booksJpaController.findBookByID(selectedBook.getId());
        List<Contributor> oldContributorList = originalBook.getContributorList();
        //List<Genre> oldGenreList = originalBook.getGenreList();
        List<BookFormats> oldFormatList = originalBook.getBookFormatsList();
        List<BookIdentifiers> oldIdentifierList = originalBook.getBookIdentifiersList();

        List<Contributor> newContributorList = selectedBook.getContributorList();
        List<BookFormats> newFormatList = selectedBook.getBookFormatsList();
        List<BookIdentifiers> newIdentifierList = selectedBook.getBookIdentifiersList();
        
//        try {
            //Delete old contributors
            for (Contributor contributor : oldContributorList) {
                if (!newContributorList.contains(contributor)) {
                    contributorJpaController.destroy(contributor.getId());
                }
            }

            //Create new contributors
            for (Contributor contributor : newContributorList) {
                if (!oldContributorList.contains(contributor)) {
                    contributorJpaController.create(contributor);
                }
            }

            //Delete old bookformats
            for (BookFormats bookFormat : oldFormatList) {
                if (!newFormatList.contains(bookFormat)) {
                    bookFormatsJpaController.destroy(bookFormat.getBookFormatsPK());
                }
            }

            //Create new bookformats
            for (BookFormats bookFormat : newFormatList) {
                if (!oldFormatList.contains(bookFormat)) {
                    bookFormatsJpaController.create(bookFormat);
                }
            }

            //Delete old bookidentifiers
            for (BookIdentifiers bookIdentifiers : oldIdentifierList) {
                if (!newIdentifierList.contains(bookIdentifiers)) {
                    bookIdentifiersJpaController.destroy(bookIdentifiers.getBookIdentifiersPK());
                }
            }

            //Create new bookidentifiers
            for (BookIdentifiers bookIdentifiers : newIdentifierList) {
                if (!oldIdentifierList.contains(bookIdentifiers)) {
                    bookIdentifiersJpaController.destroy(bookIdentifiers.getBookIdentifiersPK());
                }
            }

            selectedBook.setContributorList(newContributorList);
            selectedBook.setBookFormatsList(newFormatList);
            selectedBook.setBookIdentifiersList(newIdentifierList);
            
            booksJpaController.edit(selectedBook);
            books = booksJpaController.findAllBooks();
            
//        } catch (RollbackFailureException ex) {
//            ex.printStackTrace();
//            //Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            //Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        System.out.println(">>>>>>>>>>>>.update");
//        Books newBook = booksJpaController.findBookByID(selectedBook.getId());
//
//        try {
//            selectedBook.setBookIdentifiersList(newBook.getBookIdentifiersList());
//            selectedBook.setReviewsList(reviewsJpaController.findReviewByBookID(selectedBook.getId()));
//            selectedBook.setSalesDetailsList(newBook.getSalesDetailsList());
//
//            booksJpaController.edit(selectedBook);
//        } catch (NonexistentEntityException ex) {
//            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (RollbackFailureException ex) {
//            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        books = booksJpaController.findAllBooks();
//        System.out.println("^^^^^^^^^^^^^^^^^^OK^^^^^^^^^^^^");
        return "admin_books?faces-redirect=true";
    }

    public String cancel() {
        return "admin_books";
    }

    /**
     * ****************Edit a Book*********************
     */
    public List<ContributionType> getAllContributionType() {
        return allContributionType;
    }

    public List<Format> getAllFormats() {
        return allFormats;
    }

    public List<Genre> getAllGenre() {
        return allGenre;
    }

    public List<Contributor> getAllAuthors() {
        return allAuthors;
    }

    public List<IdentifierType> getAllIdentifierTypes() {
        return allIdentifierTypes;
    }

}
