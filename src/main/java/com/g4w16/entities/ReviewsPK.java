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

/**
 *
 * @author Dan 2016/2/17
 */
@Embeddable
public class ReviewsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Book")
    private int book;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Client")
    private int client;

    public ReviewsPK() {
    }

    public ReviewsPK(int book, int client) {
        this.book = book;
        this.client = client;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) book;
        hash += (int) client;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReviewsPK)) {
            return false;
        }
        ReviewsPK other = (ReviewsPK) object;
        if (this.book != other.book) {
            return false;
        }
        if (this.client != other.client) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.bookstoreg4w16.entities.ReviewsPK[ book=" + book + ", client=" + client + " ]";
    }
    
}
