/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "books", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b")})
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Publisher")
    private String publisher;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Pages")
    private int pages;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    //@NotNull
    @Column(name = "WholesalePrice")
    private BigDecimal wholesalePrice;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "ListPrice")
    private BigDecimal listPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SalePrice")
    private BigDecimal salePrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PubDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pubDate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "DateEntered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntered;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "RemovalStatus")
    private boolean removalStatus;
    @JoinTable(name = "bookcontributors", joinColumns = {
        @JoinColumn(name = "Book", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Contributor", referencedColumnName = "ID")})
    @ManyToMany
    private List<Contributor> contributorList;
    @JoinTable(name = "bookgenres", joinColumns = {
        @JoinColumn(name = "Book", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "Genre", referencedColumnName = "Type")})
    @ManyToMany
    private List<Genre> genreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "books")
    private List<BookIdentifiers> bookIdentifiersList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<SalesDetails> salesDetailsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "books")
    private List<Reviews> reviewsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "books")
    private List<BookFormats> bookFormatsList;

    public Books() {
        this.wholesalePrice = BigDecimal.ZERO;
        this.salePrice = BigDecimal.ZERO;
        this.dateEntered = Date.from(Instant.now());
        this.removalStatus = false;
    }

    public Books(Integer id) {
        this.id = id;
        this.wholesalePrice = BigDecimal.ZERO;
        this.salePrice = BigDecimal.ZERO;
        this.dateEntered = Date.from(Instant.now());
        this.removalStatus = false;
    }

    public Books(Integer id, String title, String publisher, String description, int pages, BigDecimal wholesalePrice, BigDecimal listPrice, BigDecimal salePrice, Date pubDate, Date dateEntered, boolean removalStatus) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.description = description;
        this.pages = pages;
        this.wholesalePrice = wholesalePrice;
        this.listPrice = listPrice;
        this.salePrice = salePrice;
        this.pubDate = pubDate;
        this.dateEntered = dateEntered;
        this.removalStatus = removalStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public boolean getRemovalStatus() {
        return removalStatus;
    }

    public void setRemovalStatus(boolean removalStatus) {
        this.removalStatus = removalStatus;
    }

    public List<Contributor> getContributorList() {
        return contributorList;
    }

    public void setContributorList(List<Contributor> contributorList) {
        this.contributorList = contributorList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<BookIdentifiers> getBookIdentifiersList() {
        return bookIdentifiersList;
    }

    public void setBookIdentifiersList(List<BookIdentifiers> bookIdentifiersList) {
        this.bookIdentifiersList = bookIdentifiersList;
    }

    public List<SalesDetails> getSalesDetailsList() {
        return salesDetailsList;
    }

    public void setSalesDetailsList(List<SalesDetails> salesDetailsList) {
        this.salesDetailsList = salesDetailsList;
    }

    public List<Reviews> getReviewsList() {
        return reviewsList;
    }
    
     public List<Reviews> getApprovedReviewsList() {
         List<Reviews> list = new ArrayList();
         for(Reviews review : reviewsList)
         {
             if(review.getApproval())
                 list.add(review);
         }
        return list;
    }

    public void setReviewsList(List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public List<BookFormats> getBookFormatsList() {
        return bookFormatsList;
    }

    public void setBookFormatsList(List<BookFormats> bookFormatsList) {
        this.bookFormatsList = bookFormatsList;
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
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.Books[ id=" + id + " ]";
    }

    public String getFormattedPubDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(pubDate);
    }

}
