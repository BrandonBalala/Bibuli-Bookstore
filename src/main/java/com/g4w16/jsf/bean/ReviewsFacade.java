/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.jsf.bean;

import com.g4w16.entities.Reviews;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ofern
 */
@Stateless
public class ReviewsFacade extends AbstractFacade<Reviews> {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReviewsFacade() {
        super(Reviews.class);
    }
    
}
