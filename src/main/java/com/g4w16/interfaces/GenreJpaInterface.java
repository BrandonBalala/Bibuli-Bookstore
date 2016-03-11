/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.Genre;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface GenreJpaInterface{

    public void create(Genre genre) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(Genre genre) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<Genre> findAllGenres();
    public List<Genre> findGenreEntities(int maxResults, int firstResult);
    public Genre findGenreByID(String id);
    public int getGenreCount();
    public boolean genreExists(String genre);

}
