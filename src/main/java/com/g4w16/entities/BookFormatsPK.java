/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author BRANDON-PC
 */
@Embeddable
public class BookFormatsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Book")
    private int book;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Format")
    private String format;

    public BookFormatsPK() {
    }

    public BookFormatsPK(int book, String format) {
        this.book = book;
        this.format = format;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) book;
        hash += (format != null ? format.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookFormatsPK)) {
            return false;
        }
        BookFormatsPK other = (BookFormatsPK) object;
        if (this.book != other.book) {
            return false;
        }
        if ((this.format == null && other.format != null) || (this.format != null && !this.format.equals(other.format))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.BookFormatsPK[ book=" + book + ", format=" + format + " ]";
    }
    
}
