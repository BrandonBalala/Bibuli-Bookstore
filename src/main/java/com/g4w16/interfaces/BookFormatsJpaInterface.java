/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookFormatsPK;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
public interface BookFormatsJpaInterface {
    
    public void create(BookFormats bookformats) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(BookFormats bookformats) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(BookFormatsPK id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<BookFormats> findAllBookFormats();
    public List<BookFormats> findBookFormatsEntities(int maxResults, int firstResult);
    public BookFormats findBookFormatByID(BookFormatsPK id);
    public int getBookFormatsCount();
    public boolean bookFormatExists(BookFormatsPK pk);
}
