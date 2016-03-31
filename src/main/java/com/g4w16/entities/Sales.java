/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author BRANDON-PC
 */
@Entity
@Table(name = "sales", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "Sales.findAll", query = "SELECT s FROM Sales s")})
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "DateEntered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntered;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "GrossValue")
    private BigDecimal grossValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NetValue")
    private BigDecimal netValue;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "Removed")
    private boolean removed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sale")
    private List<SalesDetails> salesDetailsList;
    @JoinColumn(name = "Client", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client client;

    public Sales() {
        this.dateEntered = Date.from(Instant.now());
        this.removed = false;
    }

    public Sales(Integer id) {
        this.id = id;
        this.dateEntered = Date.from(Instant.now());
        this.removed = false;
    }

    public Sales(Integer id, Date dateEntered, BigDecimal grossValue, BigDecimal netValue, boolean removed) {
        this.id = id;
        this.dateEntered = dateEntered;
        this.grossValue = grossValue;
        this.netValue = netValue;
        this.removed = removed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public BigDecimal getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(BigDecimal grossValue) {
        this.grossValue = grossValue;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public List<SalesDetails> getSalesDetailsList() {
        return salesDetailsList;
    }

    public void setSalesDetailsList(List<SalesDetails> salesDetailsList) {
        this.salesDetailsList = salesDetailsList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        if (!(object instanceof Sales)) {
            return false;
        }
        Sales other = (Sales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.Sales[ id=" + id + " ]";
    }

}
