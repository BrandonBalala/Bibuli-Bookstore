/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.persistence.BooksJpaController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private String sortBy;

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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getShortTitle(String title) {
        int length = 30;

        if (title.length() <= length) {
            return title;
        }

        return title.substring(0, 26) + "...";
    }

    public String displayProductPage(Books book) {
        productBB.setBook(book);

        return "product-page";
    }

    public void sortBooks() {
        this.sortBy = "SHEEEEITTTTTTTT";
        switch (sortBy) {
            case "cheapestFirst":
                Collections.sort(bookList, new Comparator<Books>() {
                    public int compare(Books b1, Books b2) {
                        BigDecimal priceB1 = (b1.getSalePrice() == BigDecimal.ZERO) ? b1.getListPrice() : b1.getSalePrice();
                        BigDecimal priceB2 = (b2.getSalePrice() == BigDecimal.ZERO) ? b2.getListPrice() : b2.getSalePrice();
                        return priceB2.compareTo(priceB1);
                    }
                });
                break;
            case "expensiveFirst":
                break;
            case "newestFirst":
                break;
            case "oldestFirst":
                break;
            case "highestRatingFirst":
                break;
        }

        bookList.remove(0);

        for (Books book : bookList) {
            sortBy += ", " + book.getSalePrice();
        }
    }
}
