/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.BookIdentifiersPK;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface BookIdentifiersJpaInterface{
    
    public void create(BookIdentifiers bookIdentifiers) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(BookIdentifiers bookIdentifiers) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(BookIdentifiersPK id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<BookIdentifiers> findAllBookIdentifiers();
    public List<BookIdentifiers> findBookIdentifiersEntities(int maxResults, int firstResult);
    public BookIdentifiers findBookIdentifierByID(BookIdentifiersPK id);
    public int getBookIdentifiersCount();
    public List<BookIdentifiers> findBookIdentifiersByCode(String code);
    public boolean bookIdentifierExists(BookIdentifiersPK pk);
}
