/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.interfaces;

import com.g4w16.entities.Feed;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.List;

/**
 *
 * @author Brandon Balala
 */
public interface FeedJpaInterface{

    public void create(Feed feed) throws RollbackFailureException, Exception;
    public void edit(Feed feed) throws NonexistentEntityException, RollbackFailureException, Exception;
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception;
    public List<Feed> findAllFeeds();
    public List<Feed> findFeedEntities(int maxResults, int firstResult);
    public Feed findFeedByID(Integer id);
    public int getFeedCount();
}
