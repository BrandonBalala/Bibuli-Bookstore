package com.g4w16.persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.g4w16.entities.BookFormats;
import com.g4w16.entities.Format;
import com.g4w16.interfaces.FormatJpaInterface;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import javax.inject.Named;
import javax.persistence.PersistenceContext;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Brandon Balala
 */
@Named
@RequestScoped
public class FormatJpaController implements Serializable, FormatJpaInterface{

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public FormatJpaController() {
    }

    @Override
    public void create(Format format) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (format.getBookFormatsList() == null) {
            format.setBookFormatsList(new ArrayList<BookFormats>());
        }
        try {
            utx.begin();
            List<BookFormats> attachedBookformatsList = new ArrayList<BookFormats>();
            for (BookFormats bookformatsListBookformatsToAttach : format.getBookFormatsList()) {
                bookformatsListBookformatsToAttach = em.getReference(bookformatsListBookformatsToAttach.getClass(), bookformatsListBookformatsToAttach.getBookFormatsPK());
                attachedBookformatsList.add(bookformatsListBookformatsToAttach);
            }
            format.setBookFormatsList(attachedBookformatsList);
            em.persist(format);
            for (BookFormats bookformatsListBookformats : format.getBookFormatsList()) {
                Format oldFormat1OfBookformatsListBookformats = bookformatsListBookformats.getFormat1();
                bookformatsListBookformats.setFormat1(format);
                bookformatsListBookformats = em.merge(bookformatsListBookformats);
                if (oldFormat1OfBookformatsListBookformats != null) {
                    oldFormat1OfBookformatsListBookformats.getBookFormatsList().remove(bookformatsListBookformats);
                    oldFormat1OfBookformatsListBookformats = em.merge(oldFormat1OfBookformatsListBookformats);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFormatByID(format.getType()) != null) {
                throw new PreexistingEntityException("Format " + format + " already exists.", ex);
            }
            throw ex;
        }
    }

    @Override
    public void edit(Format format) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Format persistentFormat = em.find(Format.class, format.getType());
            List<BookFormats> bookformatsListOld = persistentFormat.getBookFormatsList();
            List<BookFormats> bookformatsListNew = format.getBookFormatsList();
            List<String> illegalOrphanMessages = null;
            for (BookFormats bookformatsListOldBookformats : bookformatsListOld) {
                if (!bookformatsListNew.contains(bookformatsListOldBookformats)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bookformats " + bookformatsListOldBookformats + " since its format1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BookFormats> attachedBookformatsListNew = new ArrayList<BookFormats>();
            for (BookFormats bookformatsListNewBookformatsToAttach : bookformatsListNew) {
                bookformatsListNewBookformatsToAttach = em.getReference(bookformatsListNewBookformatsToAttach.getClass(), bookformatsListNewBookformatsToAttach.getBookFormatsPK());
                attachedBookformatsListNew.add(bookformatsListNewBookformatsToAttach);
            }
            bookformatsListNew = attachedBookformatsListNew;
            format.setBookFormatsList(bookformatsListNew);
            format = em.merge(format);
            for (BookFormats bookformatsListNewBookformats : bookformatsListNew) {
                if (!bookformatsListOld.contains(bookformatsListNewBookformats)) {
                    Format oldFormat1OfBookformatsListNewBookformats = bookformatsListNewBookformats.getFormat1();
                    bookformatsListNewBookformats.setFormat1(format);
                    bookformatsListNewBookformats = em.merge(bookformatsListNewBookformats);
                    if (oldFormat1OfBookformatsListNewBookformats != null && !oldFormat1OfBookformatsListNewBookformats.equals(format)) {
                        oldFormat1OfBookformatsListNewBookformats.getBookFormatsList().remove(bookformatsListNewBookformats);
                        oldFormat1OfBookformatsListNewBookformats = em.merge(oldFormat1OfBookformatsListNewBookformats);
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
                String id = format.getType();
                if (findFormatByID(id) == null) {
                    throw new NonexistentEntityException("The format with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Format format;
            try {
                format = em.getReference(Format.class, id);
                format.getType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The format with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BookFormats> bookformatsListOrphanCheck = format.getBookFormatsList();
            for (BookFormats bookformatsListOrphanCheckBookformats : bookformatsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Format (" + format + ") cannot be destroyed since the Bookformats " + bookformatsListOrphanCheckBookformats + " in its bookformatsList field has a non-nullable format1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(format);
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
    public List<Format> findAllFormats() {
        return findFormatEntities(true, -1, -1);
    }

    @Override
    public List<Format> findFormatEntities(int maxResults, int firstResult) {
        return findFormatEntities(false, maxResults, firstResult);
    }

    private List<Format> findFormatEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Format.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Format findFormatByID(String id) {
        return em.find(Format.class, id);
    }

    @Override
    public int getFormatCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Format> rt = cq.from(Format.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public boolean formatExists(String id){
        Format format = findFormatByID(id);
        return format != null;
    }
}
