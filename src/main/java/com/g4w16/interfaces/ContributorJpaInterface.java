/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.Contributor;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface ContributorJpaInterface {

    public void create(Contributor contributor) throws RollbackFailureException, Exception;
    public void edit(Contributor contributor) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<Contributor> findAllContributors();
    public List<Contributor> findContributorEntities(int maxResults, int firstResult);
    public Contributor findContributorByID(Integer id);
    public int getContributorCount();
    public boolean contributorExists(int contributorID);
    public int findContributorIdByNameAndType(String name, String contribution);

}
