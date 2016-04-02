/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.entities.IdentifierType;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.ContributionTypeJpaController;
import com.g4w16.persistence.ContributorJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import com.g4w16.persistence.IdentifierTypeJpaController;
import com.g4w16.persistence.ReviewsJpaController;
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
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

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
    private List<String> status;
    private List<Format> allFormats;
    private List<ContributionType> allContributionType;
    private List<Genre> allGenre;
    private List<Contributor> allAuthors;
    private List<IdentifierType> allIdentifierTypes;
    private Books newBook;

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
        newBook = new Books();
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
        selectedBook = book;
        return "admin_edit_book";
    }

    public String updateBook() {
        System.out.println(">>>>>>>>>>>>.update");
        try {
            selectedBook.setReviewsList(reviewsJpaController.findReviewByBookID(selectedBook.getId()));
            booksJpaController.edit(selectedBook);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminBooksBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        booksJpaController.findAllBooks();
        return "admin_books";
    }

    public String cancel() {
        return "admin_books";
    }

    /**
     * ****************New Book*********************
     */
    public Books getNewBook() {
        return newBook;
    }

    public void setNewBook(Books newBook) {
        this.newBook = newBook;
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
