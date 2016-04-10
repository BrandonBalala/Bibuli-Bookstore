/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Reviews;
import com.g4w16.persistence.BooksJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("resultBB")
@SessionScoped
public class ResultBackingBean implements Serializable {

    private List<Books> bookList;
    private String sortBy;

    @Inject
    private BooksJpaController bookController;

    @Inject
    private ProductPageBackingBean productBB;

    @PostConstruct
    public void init() {
        bookList = bookController.findBooksByYear(2011);
    }

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
        sortBy = "";
    }

    /**
     * Set a book
     * @param book 
     */
    public void setBook(Books book) {
        if (bookList == null) {
            bookList = new ArrayList<Books>();
        }

        bookList.add(book);
    }

    /**
     * Get sort by
     * @return 
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Set sort by
     * @param sortBy 
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Return shortened title
     * @param title
     * @return String
     */
    public String getShortTitle(String title) {
        int length = 30;

        if (title.length() <= length) {
            return title;
        }

        return title.substring(0, 26) + "...";
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

    /**
     * Sort the books by what is chosen by client
     */
    public void sortBooks() {
        switch (sortBy) {
            case "cheapestFirst":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    BigDecimal priceB1 = (b1.getSalePrice().equals(BigDecimal.ZERO)) ? b1.getListPrice() : b1.getSalePrice();
                    BigDecimal priceB2 = (b2.getSalePrice().equals(BigDecimal.ZERO)) ? b2.getListPrice() : b2.getSalePrice();
                    return priceB1.compareTo(priceB2);
                });
                break;
            case "expensiveFirst":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    BigDecimal priceB1 = (b1.getSalePrice().equals(BigDecimal.ZERO)) ? b1.getListPrice() : b1.getSalePrice();
                    BigDecimal priceB2 = (b2.getSalePrice().equals(BigDecimal.ZERO)) ? b2.getListPrice() : b2.getSalePrice();
                    return priceB2.compareTo(priceB1);
                });
                break;
            case "newestFirst":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    Date date1 = b1.getPubDate();
                    Date date2 = b2.getPubDate();
                    return date2.compareTo(date1);
                });
                break;
            case "oldestFirst":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    Date date1 = b1.getPubDate();
                    Date date2 = b2.getPubDate();
                    return date1.compareTo(date2);
                });
                break;
            case "highestRatingFirst":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    Double rating1 = getAverageRating(b1.getApprovedReviewsList());
                    Double rating2 = getAverageRating(b2.getApprovedReviewsList());
                    return rating2.compareTo(rating1);
                });
                break;
            case "topSellers":
                Collections.sort(bookList, (Books b1, Books b2) -> {
                    Integer salesCont1 = b1.getSalesDetailsList().size();
                    Integer saleCount2 = b2.getSalesDetailsList().size();
                    
                    return saleCount2.compareTo(salesCont1);
                });
                break;
        }
    }

    /**
     * Calculate average rating based on the reviews for the book
     * @param reviewList
     * @return 
     */
    private double getAverageRating(List<Reviews> reviewList) {
        if (reviewList.isEmpty()) {
            return 0.0;
        }

        int numBooks = reviewList.size();
        int sum = 0;
        for (Reviews rev : reviewList) {
            sum += rev.getRating();
        }

        return sum / numBooks;
    }
}
