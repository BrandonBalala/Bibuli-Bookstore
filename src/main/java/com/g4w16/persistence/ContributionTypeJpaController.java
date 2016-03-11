/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.ContributionType;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.Contributor;
import com.g4w16.interfaces.ContributionTypeJpaInterface;
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
public class ContributionTypeJpaController implements Serializable, ContributionTypeJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public ContributionTypeJpaController() {
    }

    @Override
    public void create(ContributionType contributionType) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (contributionType.getContributorList() == null) {
            contributionType.setContributorList(new ArrayList<Contributor>());
        }
        try {
            utx.begin();
            List<Contributor> attachedContributorList = new ArrayList<Contributor>();
            for (Contributor contributorListContributorToAttach : contributionType.getContributorList()) {
                contributorListContributorToAttach = em.getReference(contributorListContributorToAttach.getClass(), contributorListContributorToAttach.getId());
                attachedContributorList.add(contributorListContributorToAttach);
            }
            contributionType.setContributorList(attachedContributorList);
            em.persist(contributionType);
            for (Contributor contributorListContributor : contributionType.getContributorList()) {
                ContributionType oldContributionOfContributorListContributor = contributorListContributor.getContribution();
                contributorListContributor.setContribution(contributionType);
                contributorListContributor = em.merge(contributorListContributor);
                if (oldContributionOfContributorListContributor != null) {
                    oldContributionOfContributorListContributor.getContributorList().remove(contributorListContributor);
                    oldContributionOfContributorListContributor = em.merge(oldContributionOfContributorListContributor);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findContributionTypeByID(contributionType.getType()) != null) {
                throw new PreexistingEntityException("ContributionType " + contributionType + " already exists.", ex);
            }
            throw ex;
        }
    }

    @Override
    public void edit(ContributionType contributionType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            ContributionType persistentContributionType = em.find(ContributionType.class, contributionType.getType());
            List<Contributor> contributorListOld = persistentContributionType.getContributorList();
            List<Contributor> contributorListNew = contributionType.getContributorList();
            List<String> illegalOrphanMessages = null;
            for (Contributor contributorListOldContributor : contributorListOld) {
                if (!contributorListNew.contains(contributorListOldContributor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contributor " + contributorListOldContributor + " since its contribution field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contributor> attachedContributorListNew = new ArrayList<Contributor>();
            for (Contributor contributorListNewContributorToAttach : contributorListNew) {
                contributorListNewContributorToAttach = em.getReference(contributorListNewContributorToAttach.getClass(), contributorListNewContributorToAttach.getId());
                attachedContributorListNew.add(contributorListNewContributorToAttach);
            }
            contributorListNew = attachedContributorListNew;
            contributionType.setContributorList(contributorListNew);
            contributionType = em.merge(contributionType);
            for (Contributor contributorListNewContributor : contributorListNew) {
                if (!contributorListOld.contains(contributorListNewContributor)) {
                    ContributionType oldContributionOfContributorListNewContributor = contributorListNewContributor.getContribution();
                    contributorListNewContributor.setContribution(contributionType);
                    contributorListNewContributor = em.merge(contributorListNewContributor);
                    if (oldContributionOfContributorListNewContributor != null && !oldContributionOfContributorListNewContributor.equals(contributionType)) {
                        oldContributionOfContributorListNewContributor.getContributorList().remove(contributorListNewContributor);
                        oldContributionOfContributorListNewContributor = em.merge(oldContributionOfContributorListNewContributor);
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
                String id = contributionType.getType();
                if (findContributionTypeByID(id) == null) {
                    throw new NonexistentEntityException("The contributionType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            ContributionType contributionType;
            try {
                contributionType = em.getReference(ContributionType.class, id);
                contributionType.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contributionType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contributor> contributorListOrphanCheck = contributionType.getContributorList();
            for (Contributor contributorListOrphanCheckContributor : contributorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ContributionType (" + contributionType + ") cannot be destroyed since the Contributor " + contributorListOrphanCheckContributor + " in its contributorList field has a non-nullable contribution field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contributionType);
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
    public List<ContributionType> findAllContributionTypes() {
        return findContributionTypeEntities(true, -1, -1);
    }

    @Override
    public List<ContributionType> findContributionTypeEntities(int maxResults, int firstResult) {
        return findContributionTypeEntities(false, maxResults, firstResult);
    }

    private List<ContributionType> findContributionTypeEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ContributionType.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public ContributionType findContributionTypeByID(String id) {
        return em.find(ContributionType.class, id);
    }

    @Override
    public int getContributionTypeCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<ContributionType> rt = cq.from(ContributionType.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public boolean contributionypeExists(String contribution) {
        ContributionType contributionType = findContributionTypeByID(contribution);
        return contributionType != null;
    }
}
