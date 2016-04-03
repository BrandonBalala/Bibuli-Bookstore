/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import com.g4w16.entities.Client;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dan 2016/2/25
 */
@Entity
@Table(name = "billingaddress", catalog = "g4w16", schema = "")
@XmlRootElement
public class BillingAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @Column(name = "Province")
    private String province;
    @Basic(optional = false)
    @Column(name = "City")
    private String city;
    @Basic(optional = false)
    @Column(name = "FirstCivicAddress")
    private String firstCivicAddress;
    @Basic(optional = false)
    @Column(name = "SecondCivicAddress")
    private String secondCivicAddress;
    @Basic(optional = false)
    @Column(name = "PostalCode")
    private String postalCode;
    @Basic(optional = false)
    @Column(name = "Removed")
    private boolean removed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billingAddress")
    private Collection<Sales> salesCollection;
    @JoinColumn(name = "Client", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client client;

    public BillingAddress() {
    }

    public BillingAddress(Integer id) {
        this.id = id;
    }

    public BillingAddress(Integer id, String name, String province, String city, String firstCivicAddress, String secondCivicAddress, String postalCode, boolean removed) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.city = city;
        this.firstCivicAddress = firstCivicAddress;
        this.secondCivicAddress = secondCivicAddress;
        this.postalCode = postalCode;
        this.removed = removed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstCivicAddress() {
        return firstCivicAddress;
    }

    public void setFirstCivicAddress(String firstCivicAddress) {
        this.firstCivicAddress = firstCivicAddress;
    }

    public String getSecondCivicAddress() {
        return secondCivicAddress;
    }

    public void setSecondCivicAddress(String secondCivicAddress) {
        this.secondCivicAddress = secondCivicAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @XmlTransient
    public Collection<Sales> getSalesCollection() {
        return salesCollection;
    }

    public void setSalesCollection(Collection<Sales> salesCollection) {
        this.salesCollection = salesCollection;
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
        if (!(object instanceof BillingAddress)) {
            return false;
        }
        BillingAddress other = (BillingAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.BillingAddress[ id=" + id + " ]";
    }
    
}
