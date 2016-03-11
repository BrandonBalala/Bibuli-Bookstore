/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.Format;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;


/**
 *
 * @author Brandon Balala
 */
public interface FormatJpaInterface{

    public void create(Format format) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(Format format) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public List<Format> findAllFormats();
    public List<Format> findFormatEntities(int maxResults, int firstResult);
    public Format findFormatByID(String id);
    public int getFormatCount();
    public boolean formatExists(String id);
}
