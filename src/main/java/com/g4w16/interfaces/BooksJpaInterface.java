/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import java.util.List;
import com.g4w16.entities.Books;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.math.BigDecimal;

/**
 *
 * @author Brandon Balala
 */
public interface BooksJpaInterface{
    
    public void create(Books book) throws RollbackFailureException, Exception;
    public void edit(Books books) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public List<Books> findAllBooks();
    public List<Books> findBooksEntities(int maxResults, int firstResult);
    public Books findBookByID(Integer id);
    public int getBooksCount();
    public List<Books> findBooksByTitle(String title);
    public List<Books> findBooksByPublisher(String publisher);
    public List<Books> findBookByPriceRange(BigDecimal min, BigDecimal max);
    public List<Books> findBooksByYear(int year);
    public Books findBookByIdentifier(String code);
    public List<Books> findBooksByContributorName(String name);
    public List<Books> findBooksByFormat(String format);
    public List<Books> findBooksByGenre(String genre);
    public List<Books> findRecommendedBook(String genre, int bookID);
    public List<Books> findNewestBooks(int amount);
    public List<Books> findRecentlyAddedBooks(int amount);
    public List<Books> findBestSellingBook(int amount);
    //public List<Books> findBooksByRating(int rating);
    public void updateRemovalStatus(boolean status, int bookID) throws Exception;
    public boolean bookExists(int id);
}
