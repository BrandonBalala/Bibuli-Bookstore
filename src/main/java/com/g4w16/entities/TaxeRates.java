/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BRANDON-PC
 */
@Entity
@Table(name = "taxerates", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "TaxeRates.findAll", query = "SELECT t FROM TaxeRates t")})
public class TaxeRates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Province")
    private String province;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PST")
    private BigDecimal pst;
    @Column(name = "HST")
    private BigDecimal hst;
    @Column(name = "QST")
    private BigDecimal qst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public TaxeRates() {
    }

    public TaxeRates(String province) {
        this.province = province;
    }

    public TaxeRates(String province, Date updated) {
        this.province = province;
        this.updated = updated;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (province != null ? province.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxeRates)) {
            return false;
        }
        TaxeRates other = (TaxeRates) object;
        if ((this.province == null && other.province != null) || (this.province != null && !this.province.equals(other.province))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.TaxeRates[ province=" + province + " ]";
    }
    
}
