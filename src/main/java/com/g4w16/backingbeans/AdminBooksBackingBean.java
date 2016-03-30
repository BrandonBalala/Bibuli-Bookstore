/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.persistence.BooksJpaController;
import java.awt.print.Book;
import java.math.BigDecimal;
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

    
    
    
    @Inject 
    BooksJpaController booksJpaController;
    
     /**
     * For Inventory page
     */
    @PostConstruct
    public void init() {
        books = booksJpaController.findAllBooks();
        BigDecimal min=new BigDecimal(0);
        BigDecimal max=new BigDecimal(999999999);
        saleBooks=booksJpaController.findBookByPriceRange(min,max);
        
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
    
    public void onDelete(Books selectedBook){
        System.out.println(">>>>>>>>>>> setSelectedBook");
        System.out.println(selectedBook.getId());
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    
}
