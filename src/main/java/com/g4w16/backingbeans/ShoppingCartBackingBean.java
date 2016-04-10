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

    /**
     * Get book list
     * @return List<Books>
     */
    public List<Books> getBookList() {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }

        return bookList;
    }

    /**
     * Set book list
     * @param bookList 
     */
    public void setBookList(List<Books> bookList) {
        this.bookList = bookList;
    }

    /**
     * Calculate savings percentage
     * @param salePrice
     * @param retailPrice
     * @return int
     */
    public int getSavingsPercentage(BigDecimal salePrice, BigDecimal retailPrice) {
        double percentageInDecimal = (salePrice.doubleValue()) / (retailPrice.doubleValue());
        double savingsInDecimal = 1 - percentageInDecimal;
        int savingsPercentage = (int) (savingsInDecimal * 100);

        return savingsPercentage;
    }

    /**
     * Add book to cart
     * @param book 
     */
    public void addBookToCart(Books book) {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }

        bookList.add(book);
    }

    /**
     * Display product page
     * @param book
     * @return String
     */
    public String displayProductPage(Books book) {
        productBB.setBook(book);

        return "product-page";
    }

    public void removeBookFromCart(Books theBook) {
        bookList.remove(theBook);
    }

    /**
     * Calculate subtotal
     * @return 
     */
    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal temp;
        
        for(Books book : bookList){
            temp = book.getSalePrice().equals(BigDecimal.ZERO) ? book.getListPrice() : book.getSalePrice();
            
            subtotal = subtotal.add(temp);
        }
        
        return subtotal.setScale(2, RoundingMode.CEILING);
    }
    
    /**
     * Redirect to product page
     * @return 
     */
    public String proceedToCheckout(){
        return "checkout";
    }
}
