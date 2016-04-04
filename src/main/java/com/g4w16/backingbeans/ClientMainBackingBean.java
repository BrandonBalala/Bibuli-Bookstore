/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Banner;
import com.g4w16.entities.Books;
import com.g4w16.entities.Genre;
import com.g4w16.persistence.BannerJpaController;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.PollJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 1311213
 */
@Named("ClientMainBB")
@SessionScoped
public class ClientMainBackingBean implements Serializable {

    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    @Inject
    private BooksJpaController bookJpaController;

    @Inject
    private PollJpaController pollJpaController;

    @Inject
    private ProductPageBackingBean productBB;

    @Inject
    private BannerJpaController bannerControler;

    private List<Books> bestSellerBooks;
    private List<Books> recentlyAddedBooks;
    private List<Books> newestReleases;
    private List<Books> recommendedBooks;
    private List<Banner> bannerList;

    private static final int NUM_BOOKS = 12;

    public boolean bestSellers() {
        List<Books> container = bookJpaController.findBestSellingBook(NUM_BOOKS);
        if (container.isEmpty()) {
            return false;
        } else {
            bestSellerBooks = container;
            return true;
        }

    }

    public List<Books> getBestSellersBooks() {
        return bestSellerBooks;
    }

    public boolean recentlyAdded() {
        List<Books> container = bookJpaController.findRecentlyAddedBooks(NUM_BOOKS);
        if (container.isEmpty()) {
            return false;
        } else {
            recentlyAddedBooks = container;
            return true;
        }
    }

    public List<Books> getRecentlyAddedBooks() {
        return recentlyAddedBooks;
    }

    public boolean newestReleases() {
        List<Books> container = bookJpaController.findNewestBooks(NUM_BOOKS);
        if (container.isEmpty()) {
            return false;
        } else {
            newestReleases = container;
            return true;
        }
    }

    public List<Books> getNewestReleases() {
        return newestReleases;
    }

    public boolean recommendedBooks() {
        if (session.getAttribute("lastGenre") == null || ((List<Genre>) session.getAttribute("lastGenre")).isEmpty()) {
            return false;
        }

        //GET A GENRE
        List<Genre> genreList = (List<Genre>) session.getAttribute("lastGenre");
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(genreList.size());
        //If book has multipe genres pick one genre at random to display in recommended books carousel
        String genre = genreList.get(index).getType();

        //GET THE RECOMMMENDED BOOKS
        List<Books> container = bookJpaController.findBooksByGenre(genre);
        if (container.isEmpty()) {
            return false;
        }

        //GET x AMOUNT OF BOOKS
        Collections.shuffle(container);
        int numBooks = NUM_BOOKS;

        //To avoid index out of bounds exception
        if (numBooks > container.size()) {
            numBooks = container.size();
        }
        recommendedBooks = container.subList(0, numBooks);

        return true;
    }

    public List<Books> getRecommendedBooks() {
        return recommendedBooks;
    }
    
    public List<Banner> getBannerList(){
        return bannerList;
    }

    public String displayProductPage(Books book) {
        productBB.setBook(book);

        return "product-page";
    }

    @PostConstruct
    public void init() {
        bestSellerBooks = new ArrayList<Books>();
        recentlyAddedBooks = new ArrayList<Books>();
        newestReleases = new ArrayList<Books>();
        recommendedBooks = new ArrayList<Books>();
        bannerList = new ArrayList<Banner>();
        
        bannerList = bannerControler.findSelectedBanners();
    }
}
