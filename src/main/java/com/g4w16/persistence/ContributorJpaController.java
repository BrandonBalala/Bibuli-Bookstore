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
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Books;
import com.g4w16.entities.Contributor;
import com.g4w16.interfaces.ContributorJpaInterface;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Brandon Balala
 */
@Named
@RequestScoped
public class ContributorJpaController implements Serializable, ContributorJpaInterface {

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Inject
    private ContributionTypeJpaController contributionTypeController;

    /**
     * Default constructor
     */
    public ContributorJpaController() {
    }

    @Override
    public void create(Contributor contributor) throws RollbackFailureException, Exception {
        if (contributor.getBooksList() == null) {
            contributor.setBooksList(new ArrayList<Books>());
        }

        if (!contributionTypeController.contributionypeExists(contributor.getContribution().getType())) {
            throw new NonexistentEntityException("Identifier type does not exist");
        }

        try {
            utx.begin();
            ContributionType contribution = contributor.getContribution();
            if (contribution != null) {
                contribution = em.getReference(contribution.getClass(), contribution.getType());
                contributor.setContribution(contribution);
            }
            List<Books> attachedBooksList = new ArrayList<Books>();
            for (Books booksListBooksToAttach : contributor.getBooksList()) {
                booksListBooksToAttach = em.getReference(booksListBooksToAttach.getClass(), booksListBooksToAttach.getId());
                attachedBooksList.add(booksListBooksToAttach);
            }
            contributor.setBooksList(attachedBooksList);
            em.persist(contributor);
            System.out.println("ID AFTER PERSIST: " + contributor.getId());
            if (contribution != null) {
                contribution.getContributorList().add(contributor);
                contribution = em.merge(contribution);
            }
            for (Books booksListBooks : contributor.getBooksList()) {
                booksListBooks.getContributorList().add(contributor);
                booksListBooks = em.merge(booksListBooks);
            }
            utx.commit();
            System.out.println("ID AFTER COMMIT " + contributor.getId());
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
    public void edit(Contributor contributor) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Contributor persistentContributor = em.find(Contributor.class, contributor.getId());
            ContributionType contributionOld = persistentContributor.getContribution();
            ContributionType contributionNew = contributor.getContribution();
            List<Books> booksListOld = persistentContributor.getBooksList();
            List<Books> booksListNew = contributor.getBooksList();
            if (contributionNew != null) {
                contributionNew = em.getReference(contributionNew.getClass(), contributionNew.getType());
                contributor.setContribution(contributionNew);
            }
            List<Books> attachedBooksListNew = new ArrayList<Books>();
            for (Books booksListNewBooksToAttach : booksListNew) {
                booksListNewBooksToAttach = em.getReference(booksListNewBooksToAttach.getClass(), booksListNewBooksToAttach.getId());
                attachedBooksListNew.add(booksListNewBooksToAttach);
            }
            booksListNew = attachedBooksListNew;
            contributor.setBooksList(booksListNew);
            contributor = em.merge(contributor);
            if (contributionOld != null && !contributionOld.equals(contributionNew)) {
                contributionOld.getContributorList().remove(contributor);
                contributionOld = em.merge(contributionOld);
            }
            if (contributionNew != null && !contributionNew.equals(contributionOld)) {
                contributionNew.getContributorList().add(contributor);
                contributionNew = em.merge(contributionNew);
            }
            for (Books booksListOldBooks : booksListOld) {
                if (!booksListNew.contains(booksListOldBooks)) {
                    booksListOldBooks.getContributorList().remove(contributor);
                    booksListOldBooks = em.merge(booksListOldBooks);
                }
            }
            for (Books booksListNewBooks : booksListNew) {
                if (!booksListOld.contains(booksListNewBooks)) {
                    booksListNewBooks.getContributorList().add(contributor);
                    booksListNewBooks = em.merge(booksListNewBooks);
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
                Integer id = contributor.getId();
                if (findContributorByID(id) == null) {
                    throw new NonexistentEntityException("The contributor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Contributor contributor;
            try {
                contributor = em.getReference(Contributor.class, id);
                contributor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contributor with id " + id + " no longer exists.", enfe);
            }
            ContributionType contribution = contributor.getContribution();
            if (contribution != null) {
                contribution.getContributorList().remove(contributor);
                contribution = em.merge(contribution);
            }
            List<Books> booksList = contributor.getBooksList();
            for (Books booksListBooks : booksList) {
                booksListBooks.getContributorList().remove(contributor);
                booksListBooks = em.merge(booksListBooks);
            }
            em.remove(contributor);
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
    public List<Contributor> findAllContributors() {
        return findContributorEntities(true, -1, -1);
    }

    @Override
    public List<Contributor> findContributorEntities(int maxResults, int firstResult) {
        return findContributorEntities(false, maxResults, firstResult);
    }

    private List<Contributor> findContributorEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Contributor.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Contributor findContributorByID(Integer id) {
        return em.find(Contributor.class, id);
    }

    @Override
    public int getContributorCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Contributor> rt = cq.from(Contributor.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public boolean contributorExists(int contributorID) {
        Contributor result = findContributorByID(contributorID);
        return result != null;
    }

    @Override
    public int findContributorIdByNameAndType(String name, String contribution) {
        Query q = em.createQuery("SELECT c.id FROM Contributor c WHERE c.name = :name AND c.contribution = :contribution");
        q.setParameter("name", name);
        q.setParameter("contribution", new ContributionType(contribution));

        int contributorID;
        try {
            contributorID = (int) q.getSingleResult();
        } catch (NoResultException ex) {
            contributorID = -1;
        }

        return contributorID;
    }

    public Contributor findContributorByName(String name, String contribution) {
        Query q = em.createQuery("SELECT c FROM Contributor c WHERE c.name = :name AND c.contribution = :contribution");
        q.setParameter("name", name);
        q.setParameter("contribution", new ContributionType(contribution));
        return (Contributor) q.getSingleResult();
    }
}
