/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BRANDON-PC
 */
@Entity
@Table(name = "bookidentifiers", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "BookIdentifiers.findAll", query = "SELECT b FROM BookIdentifiers b")})
public class BookIdentifiers implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BookIdentifiersPK bookIdentifiersPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Code")
    private String code;
    @JoinColumn(name = "Book", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;
    @JoinColumn(name = "Type", referencedColumnName = "Type", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IdentifierType identifierType;

    public BookIdentifiers() {
    }

    public BookIdentifiers(BookIdentifiersPK bookIdentifiersPK) {
        this.bookIdentifiersPK = bookIdentifiersPK;
    }

    public BookIdentifiers(BookIdentifiersPK bookIdentifiersPK, String code) {
        this.bookIdentifiersPK = bookIdentifiersPK;
        this.code = code;
    }

    public BookIdentifiers(int book, String type) {
        this.bookIdentifiersPK = new BookIdentifiersPK(book, type);
    }

    public BookIdentifiersPK getBookIdentifiersPK() {
        return bookIdentifiersPK;
    }

    public void setBookIdentifiersPK(BookIdentifiersPK bookIdentifiersPK) {
        this.bookIdentifiersPK = bookIdentifiersPK;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookIdentifiersPK != null ? bookIdentifiersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookIdentifiers)) {
            return false;
        }
        BookIdentifiers other = (BookIdentifiers) object;
        if ((this.bookIdentifiersPK == null && other.bookIdentifiersPK != null) || (this.bookIdentifiersPK != null && !this.bookIdentifiersPK.equals(other.bookIdentifiersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return identifierType.toString();
    }
    
}
