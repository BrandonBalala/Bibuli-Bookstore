/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.TaxeRates;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangd
 */
@Named("booksBB")
@RequestScoped
public class AdminBooksBackingBean {

    private List<Books> books;
    private List<Books> saleBooks;
    private List<Books> filteredBooks;
    private Books selectedBook;
    private List<String> status;
    private List<Format> formats;
    private List<Format> selectedFormats;

    @Inject
    BooksJpaController booksJpaController;

    @Inject
    GenreJpaController genreJpaController;
    
    @Inject
    FormatJpaController formatJpaController;

    /**
     * For Inventory page
     */
    @PostConstruct
    public void init() {
        books = booksJpaController.findAllBooks();
        BigDecimal min = new BigDecimal(0);
        BigDecimal max = new BigDecimal(999999999);
        saleBooks = booksJpaController.findBookByPriceRange(min, max);
        
        formats=formatJpaController.findAllFormats();
    }

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
        System.out.println(">>>>>>>>>>> getSelectedBook");
        return selectedBook;
    }

    public void setSelectedBook(Books selectedBook) {
        System.out.println(">>>>>>>>>>> setSelectedBook");
        this.selectedBook = selectedBook;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public List<Format> getSelectedFormats() {
        return selectedFormats;
    }

    public void setSelectedFormats(List<Format> selectedFormats) {
        this.selectedFormats = selectedFormats;
    }

    

    

    
    
    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        Books editedBook = (Books) event.getObject();
        booksJpaController.edit(editedBook);
    }

    public void onRowCancel(RowEditEvent event) {
    }
    
    public String showDetail(int id){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>"+id);
        return "admin_add_book";
        
    }

}
