/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.IdentifierType;
import com.g4w16.interfaces.IdentifierTypeJpaInterface;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Brandon Balala
 */
@Named
@RequestScoped
public class IdentifierTypeJpaController implements Serializable, IdentifierTypeJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public IdentifierTypeJpaController() {
    }

    @Override
    public void create(IdentifierType identifierType) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (identifierTypeExists(identifierType.getType())) {
            throw new PreexistingEntityException("IdentifierType " + identifierType + " already exists.");
        }

        if (identifierType.getBookIdentifiersList() == null) {
            identifierType.setBookIdentifiersList(new ArrayList<BookIdentifiers>());
        }
        try {
            utx.begin();
            List<BookIdentifiers> attachedBookIdentifiersList = new ArrayList<BookIdentifiers>();
            for (BookIdentifiers bookIdentifiersListBookIdentifiersToAttach : identifierType.getBookIdentifiersList()) {
                bookIdentifiersListBookIdentifiersToAttach = em.getReference(bookIdentifiersListBookIdentifiersToAttach.getClass(), bookIdentifiersListBookIdentifiersToAttach.getBookIdentifiersPK());
                attachedBookIdentifiersList.add(bookIdentifiersListBookIdentifiersToAttach);
            }
            identifierType.setBookIdentifiersList(attachedBookIdentifiersList);
            em.persist(identifierType);
            for (BookIdentifiers bookIdentifiersListBookIdentifiers : identifierType.getBookIdentifiersList()) {
                IdentifierType oldIdentifierTypeOfBookIdentifiersListBookIdentifiers = bookIdentifiersListBookIdentifiers.getIdentifierType();
                bookIdentifiersListBookIdentifiers.setIdentifierType(identifierType);
                bookIdentifiersListBookIdentifiers = em.merge(bookIdentifiersListBookIdentifiers);
                if (oldIdentifierTypeOfBookIdentifiersListBookIdentifiers != null) {
                    oldIdentifierTypeOfBookIdentifiersListBookIdentifiers.getBookIdentifiersList().remove(bookIdentifiersListBookIdentifiers);
                    oldIdentifierTypeOfBookIdentifiersListBookIdentifiers = em.merge(oldIdentifierTypeOfBookIdentifiersListBookIdentifiers);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    @Override
    public void edit(IdentifierType identifierType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            IdentifierType persistentIdentifierType = em.find(IdentifierType.class, identifierType.getType());
            List<BookIdentifiers> bookIdentifiersListOld = persistentIdentifierType.getBookIdentifiersList();
            List<BookIdentifiers> bookIdentifiersListNew = identifierType.getBookIdentifiersList();
            List<String> illegalOrphanMessages = null;
            for (BookIdentifiers bookIdentifiersListOldBookIdentifiers : bookIdentifiersListOld) {
                if (!bookIdentifiersListNew.contains(bookIdentifiersListOldBookIdentifiers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookIdentifiers " + bookIdentifiersListOldBookIdentifiers + " since its identifierType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BookIdentifiers> attachedBookIdentifiersListNew = new ArrayList<BookIdentifiers>();
            for (BookIdentifiers bookIdentifiersListNewBookIdentifiersToAttach : bookIdentifiersListNew) {
                bookIdentifiersListNewBookIdentifiersToAttach = em.getReference(bookIdentifiersListNewBookIdentifiersToAttach.getClass(), bookIdentifiersListNewBookIdentifiersToAttach.getBookIdentifiersPK());
                attachedBookIdentifiersListNew.add(bookIdentifiersListNewBookIdentifiersToAttach);
            }
            bookIdentifiersListNew = attachedBookIdentifiersListNew;
            identifierType.setBookIdentifiersList(bookIdentifiersListNew);
            identifierType = em.merge(identifierType);
            for (BookIdentifiers bookIdentifiersListNewBookIdentifiers : bookIdentifiersListNew) {
                if (!bookIdentifiersListOld.contains(bookIdentifiersListNewBookIdentifiers)) {
                    IdentifierType oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers = bookIdentifiersListNewBookIdentifiers.getIdentifierType();
                    bookIdentifiersListNewBookIdentifiers.setIdentifierType(identifierType);
                    bookIdentifiersListNewBookIdentifiers = em.merge(bookIdentifiersListNewBookIdentifiers);
                    if (oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers != null && !oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers.equals(identifierType)) {
                        oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers.getBookIdentifiersList().remove(bookIdentifiersListNewBookIdentifiers);
                        oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers = em.merge(oldIdentifierTypeOfBookIdentifiersListNewBookIdentifiers);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = identifierType.getType();
                if (findIdentifierTypeByID(id) == null) {
                    throw new NonexistentEntityException("The identifierType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }
	
    @Override
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            IdentifierType identifierType;
            try {
                identifierType = em.getReference(IdentifierType.class, id);
                identifierType.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The identifierType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BookIdentifiers> bookIdentifiersListOrphanCheck = identifierType.getBookIdentifiersList();
            for (BookIdentifiers bookIdentifiersListOrphanCheckBookIdentifiers : bookIdentifiersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This IdentifierType (" + identifierType + ") cannot be destroyed since the BookIdentifiers " + bookIdentifiersListOrphanCheckBookIdentifiers + " in its bookIdentifiersList field has a non-nullable identifierType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(identifierType);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    @Override
    public List<IdentifierType> findAllIdentifierTypes() {
        return findIdentifierTypeEntities(true, -1, -1);
    }

    @Override
    public List<IdentifierType> findIdentifierTypeEntities(int maxResults, int firstResult) {
        return findIdentifierTypeEntities(false, maxResults, firstResult);
    }

    private List<IdentifierType> findIdentifierTypeEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(IdentifierType.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public IdentifierType findIdentifierTypeByID(String id) {
        return em.find(IdentifierType.class, id);
    }

    @Override
    public int getIdentifierTypeCont() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<IdentifierType> rt = cq.from(IdentifierType.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public boolean identifierTypeExists(String identifier) {
        IdentifierType identifierType = findIdentifierTypeByID(identifier);
        return identifierType != null;
    }
}
