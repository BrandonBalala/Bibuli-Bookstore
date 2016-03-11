/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.ContributionType;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface ContributionTypeJpaInterface {

    public void create(ContributionType contributionType) throws PreexistingEntityException, RollbackFailureException, Exception;
    public void edit(ContributionType contributionType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception;
    public List<ContributionType> findAllContributionTypes();
    public List<ContributionType> findContributionTypeEntities(int maxResults, int firstResult);
    public ContributionType findContributionTypeByID(String id);
    public int getContributionTypeCount();
    public boolean contributionypeExists(String contribution);

}
