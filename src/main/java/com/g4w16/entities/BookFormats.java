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
@Table(name = "bookformats", catalog = "g4w16", schema = "")
//@NamedQueries({
//    @NamedQuery(name = "BookFormats.findAll", query = "SELECT b FROM BookFormats b")})
public class BookFormats implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BookFormatsPK bookFormatsPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "File")
    private String file;
    @JoinColumn(name = "Book", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Books books;
    @JoinColumn(name = "Format", referencedColumnName = "Type", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Format format1;

    public BookFormats() {
    }

    public BookFormats(BookFormatsPK bookFormatsPK) {
        this.bookFormatsPK = bookFormatsPK;
    }

    public BookFormats(BookFormatsPK bookFormatsPK, String file) {
        this.bookFormatsPK = bookFormatsPK;
        this.file = file;
    }

    public BookFormats(int book, String format) {
        this.bookFormatsPK = new BookFormatsPK(book, format);
    }

    public BookFormatsPK getBookFormatsPK() {
        return bookFormatsPK;
    }

    public void setBookFormatsPK(BookFormatsPK bookFormatsPK) {
        this.bookFormatsPK = bookFormatsPK;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public Format getFormat1() {
        return format1;
    }

    public void setFormat1(Format format1) {
        this.format1 = format1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookFormatsPK != null ? bookFormatsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookFormats)) {
            return false;
        }
        BookFormats other = (BookFormats) object;
        if ((this.bookFormatsPK == null && other.bookFormatsPK != null) || (this.bookFormatsPK != null && !this.bookFormatsPK.equals(other.bookFormatsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.BookFormats[ bookFormatsPK=" + bookFormatsPK + " ]";
    }
    
}
