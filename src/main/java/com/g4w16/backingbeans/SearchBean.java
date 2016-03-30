/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Genre;
import com.g4w16.persistence.BooksJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Brandon Balala
 */
@Named("searchBean")
@SessionScoped
public class SearchBean implements Serializable {

    private String keywords;
    private String searchBy;

    @Inject
    private BooksJpaController bookController;

    @Inject
    private ResultBackingBean resultBB;

    @Inject
    private ProductPageBackingBean productBB;

    public String getKeywords() {
        if (keywords == null) {
            return "";
        }

        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSearchBy() {
        if (searchBy == null) {
            return "";
        }

        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String displayBooks(List<Books> bookList) {
        if (bookList.size() == 1) {
            productBB.setBook(bookList.get(0));
            return "product-page";
        } else {
            resultBB.setBookList(bookList);
            return "results";
        }
    }

    public String advancedSearchBooks() {
        List<Books> books = new ArrayList<Books>();

        switch (searchBy) {
            case "all":
                return searchBooks();
            case "title":
                books = findBooksByTitle(keywords);
                break;
            case "identifier":
                books = findBooksByIdentifier(keywords);
                break;
            case "contributor":
                books = findBooksByContributor(keywords);
                break;
            case "publisher":
                books = findBooksByPublisher(keywords);
                break;
            case "year":
                books = findBooksByYear(keywords);
                break;
            case "genre":
                books = findBooksByGenre(keywords);
                break;
            case "format":
                books = findBooksByFormat(keywords);
                break;
        }
        clearFields();

        if (books == null || books.isEmpty()) {
            books = new ArrayList<Books>();
        }

        return displayBooks(books);
    }

    public String searchBooks() {
        if (keywords.isEmpty()) {
            return "advanced-search";
        }

        List<List<Books>> listOfLists = new ArrayList<List<Books>>();
        listOfLists.add(findBooksByGenre(keywords));
        listOfLists.add(findBooksByIdentifier(keywords));
        listOfLists.add(findBooksByContributor(keywords));
        listOfLists.add(findBooksByFormat(keywords));
        listOfLists.add(findBooksByPublisher(keywords));
        listOfLists.add(findBooksByTitle(keywords));
        listOfLists.add(findBooksByYear(keywords));

        clearFields();

        List<Books> books = new ArrayList<Books>();

        List<Books> tempBookList;
        for (int cntr = 0; cntr < listOfLists.size(); cntr++) {
            tempBookList = listOfLists.get(cntr);

            if (tempBookList == null) {
                continue;
            }

            if (tempBookList.isEmpty()) {
                continue;
            }

            books.addAll(tempBookList);
        }

        //Remove duplicates
        Set<Books> bookSet = new HashSet<Books>();
        bookSet.addAll(books);
        books.clear();
        books.addAll(bookSet);

        return displayBooks(books);
    }

    public List<Books> findBooksByGenre(String genre) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        List<Genre> list = new ArrayList();
        Genre g = new Genre();
        g.setType(genre);
        list.add(g);
        session.setAttribute("lastGenre",list);
        return bookController.findBooksByGenre(genre);
    }

    public List<Books> findBooksByIdentifier(String identifier) {
        List<Books> bookList = new ArrayList<Books>();
        Books book;

        try {
            book = bookController.findBookByIdentifier(identifier);
        } catch (Exception e) {
            return null;
        }

        bookList.add(bookController.findBookByIdentifier(identifier));

        return bookList;
    }

    public List<Books> findBooksByContributor(String contributor) {
        return bookController.findBooksByContributorName(contributor);
    }

    public List<Books> findBooksByFormat(String format) {
        return bookController.findBooksByFormat(format);
    }

    public List<Books> findBooksByPublisher(String publisher) {
        return bookController.findBooksByPublisher(publisher);
    }

    public List<Books> findBooksByTitle(String title) {
        return bookController.findBooksByTitle(title);
    }

    public List<Books> findBooksByYear(String yearString) {
        int year;

        try {
            year = Integer.valueOf(yearString);
        } catch (Exception e) {
            return null;
        }

        return bookController.findBooksByYear(year);
    }

    private void clearFields() {
        keywords = "";
        searchBy = "";
    }
}
