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
import java.util.ArrayList;
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
    
    @Inject
    private ProductPageBackingBean productBB;
    
    private List<Books> bestSellerBooks = new ArrayList<Books>();
    private List<Books> recentlyAddedBooks = new ArrayList<Books>();
    
    public boolean bestSellers()
    {
       List<Books> container = bookJpaController.findBestSellingBook(12);
       if(container.isEmpty())
       {
            return false;
       }
       else
       {
           bestSellerBooks = container;
           return true;
       }

    }
    
     public List<Books> getBestSellersBooks()
    {
      return bestSellerBooks;
    }
    
    public boolean recentlyAdded()
    {
      List<Books> container = bookJpaController.findRecentlyAddedBooks(12);
      if(container.isEmpty())
       {
            return false;
       }
       else
       {
           recentlyAddedBooks = container;
           return true;
       }
    }
    
    public List<Books> getRecentlyAddedBooks()
    {
      return recentlyAddedBooks;
    }
    
    public String displayProductPage(Books book) {
        productBB.setBook(book);

        return "product-page";
    }
}
