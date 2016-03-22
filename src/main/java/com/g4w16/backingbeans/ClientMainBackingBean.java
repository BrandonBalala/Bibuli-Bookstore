/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.PollJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author 1311213
 */
@Named("ClientMainBB")
@SessionScoped
public class ClientMainBackingBean implements Serializable{
    
    @Inject
    private BooksJpaController bookJpaController;
    
    @Inject
    private PollJpaController pollJpaController;
    
    public List<Books> getBestSellers()
    {
       return  bookJpaController.findBestSellingBook(12);
    }
    
    public List<Books> getRecentlyAdded()
    {
      return bookJpaController.findRecentlyAddedBooks(12);
    }
    
    public void getBestSellersCount()
    {
       System.out.print(bookJpaController.findRecentlyAddedBooks(12).size());
    }
}
