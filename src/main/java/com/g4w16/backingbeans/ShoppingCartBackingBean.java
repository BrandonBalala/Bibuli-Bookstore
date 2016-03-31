/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.persistence.BooksJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("cartBB")
@SessionScoped
public class ShoppingCartBackingBean implements Serializable {

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

    public int getSavingsPercentage(BigDecimal salePrice, BigDecimal retailPrice) {
        double percentageInDecimal = (salePrice.doubleValue()) / (retailPrice.doubleValue());
        double savingsInDecimal = 1 - percentageInDecimal;
        int savingsPercentage = (int) (savingsInDecimal * 100);

        return savingsPercentage;
    }

    public void addBookToCart(Books book) {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }

        bookList.add(book);
    }

    public String displayProductPage(Books book) {
        productBB.setBook(book);

        return "product-page";
    }

    public void removeBookFromCart(Books theBook) {
        bookList.remove(theBook);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal temp;
        
        for(Books book : bookList){
            temp = book.getSalePrice().equals(BigDecimal.ZERO) ? book.getListPrice() : book.getSalePrice();
            
            subtotal = subtotal.add(temp);
        }
        
        return subtotal.setScale(2, RoundingMode.CEILING);
    }
    
    public String proceedToCheckout(){

        //TODO
        
        return "checkout";
    }
}
