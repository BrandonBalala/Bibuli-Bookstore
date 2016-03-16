/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.persistence.BooksJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("resultBB")
@RequestScoped
public class ResultBackingBean {

    private List<Books> bookList;

    @Inject
    private BooksJpaController bookController;
    
    @Inject
    private ProductPageBackingBean productBB;

    public List<Books> getBookList() {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }
        
        return bookList;
    }

    public void setBookList(List<Books> bookList) {
        this.bookList = bookList;
    }

    public void setBook(Books book) {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }

        bookList.add(book);
    }
    
    public String getShortTitle(String title){
        int length = 30;
        
        if(title.length() <= length){
            return title;
        }
        
        return title.substring(0, 26) + "...";
    }
    
    public String displayProductPage(Books book){
//        Books book = bookController.findBookByID(id);
        
        //productBB = new ProductPageBackingBean();
        productBB.setBook(book);
        
        return "product-page";
    }
}
