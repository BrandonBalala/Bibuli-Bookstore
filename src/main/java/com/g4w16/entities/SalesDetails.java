/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author BRANDON-PC
 */
@Entity
@Table(name = "salesdetails", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "SalesDetails.findAll", query = "SELECT s FROM SalesDetails s")})
public class SalesDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Price")
    private BigDecimal price;
    @Column(name = "PST")
    private BigDecimal pst;
    @Column(name = "HST")
    private BigDecimal hst;
    @Column(name = "QST")
    private BigDecimal qst;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "Removed")
    private boolean removed;
    @JoinColumn(name = "Sale", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Sales sale;
    @JoinColumn(name = "Book", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Books book;

    public SalesDetails() {
        this.removed = false;
    }

    public SalesDetails(Integer id) {
        this.id = id;
        this.removed = false;
    }

    public SalesDetails(Integer id, BigDecimal price, boolean removed) {
        this.id = id;
        this.price = price;
        this.removed = removed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPst() {
        return pst;
    }

    public void setPst(BigDecimal pst) {
        this.pst = pst;
    }

    public BigDecimal getHst() {
        return hst;
    }

    public void setHst(BigDecimal hst) {
        this.hst = hst;
    }

    public BigDecimal getQst() {
        return qst;
    }

    public void setQst(BigDecimal qst) {
        this.qst = qst;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Sales getSale() {
        return sale;
    }

    public void setSale(Sales sale) {
        this.sale = sale;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalesDetails)) {
            return false;
        }
        SalesDetails other = (SalesDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.SalesDetails[ id=" + id + " ]";
    }
    
}
