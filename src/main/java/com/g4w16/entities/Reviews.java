/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.ReviewsPK;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dan 2016/2/17
 */
@Entity
@Table(name = "reviews", catalog = "g4w16", schema = "")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r"),
//    @NamedQuery(name = "Reviews.findByBook", query = "SELECT r FROM Reviews r WHERE r.reviewsPK.book = :book"),
//    @NamedQuery(name = "Reviews.findByCreationDate", query = "SELECT r FROM Reviews r WHERE r.creationDate = :creationDate"),
//    @NamedQuery(name = "Reviews.findByClient", query = "SELECT r FROM Reviews r WHERE r.reviewsPK.client = :client"),
//    @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating"),
//    @NamedQuery(name = "Reviews.findByApproval", query = "SELECT r FROM Reviews r WHERE r.approval = :approval"),
///*custome query*/
//    @NamedQuery(name = "Reviews.findByPK", query ="SELECT r FROM Reviews r WHERE r.reviewsPK.book = :book and r.reviewsPK.client = :client"),
//    @NamedQuery(name = "Reviews.updateRemovalStatus", query ="UPDATE r SET APPROVAL=TRUE WHERE r.reviewsPK.book = :book and r.reviewsPK.client = :client")
//})
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReviewsPK reviewsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CreationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Rating")
    private int rating;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "Text")
    private String text;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Approval")
    private boolean approval;
    @JoinColumn(name = "Book", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;
    @JoinColumn(name = "Client", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client1;

    public Reviews() {
    }

    public Reviews(ReviewsPK reviewsPK) {
        this.reviewsPK = reviewsPK;
    }

    public Reviews(ReviewsPK reviewsPK, Date creationDate, int rating, String text, boolean approval) {
        this.reviewsPK = reviewsPK;
        this.creationDate = creationDate;
        this.rating = rating;
        this.text = text;
        this.approval = approval;
    }

    public Reviews(int book, int client) {
        this.reviewsPK = new ReviewsPK(book, client);
    }

    public ReviewsPK getReviewsPK() {
        return reviewsPK;
    }

    public void setReviewsPK(ReviewsPK reviewsPK) {
        this.reviewsPK = reviewsPK;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Client getClient1() {
        return client1;
    }

    public void setClient1(Client client1) {
        this.client1 = client1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewsPK != null ? reviewsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.reviewsPK == null && other.reviewsPK != null) || (this.reviewsPK != null && !this.reviewsPK.equals(other.reviewsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.bookstoreg4w16.entities.Reviews[ reviewsPK=" + reviewsPK + " ]";
    }

    public String getFormattedReviewCreationDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(creationDate);
    }

}
