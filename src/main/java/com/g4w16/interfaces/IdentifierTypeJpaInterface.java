/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.IdentifierType;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface IdentifierTypeJpaInterface{

    public void create(IdentifierType identifierType) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(IdentifierType identifierType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public List<IdentifierType> findAllIdentifierTypes();
    public List<IdentifierType> findIdentifierTypeEntities(int maxResults, int firstResult);
    public IdentifierType findIdentifierTypeByID(String id);
    public int getIdentifierTypeCont();
    public boolean identifierTypeExists(String identifier);

}
