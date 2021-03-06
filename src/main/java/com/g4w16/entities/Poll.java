/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wangd
 */
@Entity
@Table(name = "poll")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Poll.findAll", query = "SELECT p FROM Poll p"),
//    @NamedQuery(name = "Poll.findById", query = "SELECT p FROM Poll p WHERE p.id = :id"),
//    @NamedQuery(name = "Poll.findBySelected", query = "SELECT p FROM Poll p WHERE p.selected = :selected"),
//    @NamedQuery(name = "Poll.findByQuestion", query = "SELECT p FROM Poll p WHERE p.question = :question"),
//    @NamedQuery(name = "Poll.findByFirstAnswer", query = "SELECT p FROM Poll p WHERE p.firstAnswer = :firstAnswer"),
//    @NamedQuery(name = "Poll.findBySecondAnswer", query = "SELECT p FROM Poll p WHERE p.secondAnswer = :secondAnswer"),
//    @NamedQuery(name = "Poll.findByThirdAnswer", query = "SELECT p FROM Poll p WHERE p.thirdAnswer = :thirdAnswer"),
//    @NamedQuery(name = "Poll.findByFourthAnswer", query = "SELECT p FROM Poll p WHERE p.fourthAnswer = :fourthAnswer"),
//    @NamedQuery(name = "Poll.findByFirstCount", query = "SELECT p FROM Poll p WHERE p.firstCount = :firstCount"),
//    @NamedQuery(name = "Poll.findBySecondCount", query = "SELECT p FROM Poll p WHERE p.secondCount = :secondCount"),
//    @NamedQuery(name = "Poll.findByThirdCount", query = "SELECT p FROM Poll p WHERE p.thirdCount = :thirdCount"),
//    @NamedQuery(name = "Poll.findByFourthCount", query = "SELECT p FROM Poll p WHERE p.fourthCount = :fourthCount")})
public class Poll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Selected")
    private boolean selected;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "Question")
    private String question;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "FirstAnswer")
    private String firstAnswer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "SecondAnswer")
    private String secondAnswer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ThirdAnswer")
    private String thirdAnswer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "FourthAnswer")
    private String fourthAnswer;
    @Column(name = "FirstCount")
    private Integer firstCount;
    @Column(name = "SecondCount")
    private Integer secondCount;
    @Column(name = "ThirdCount")
    private Integer thirdCount;
    @Column(name = "FourthCount")
    private Integer fourthCount;

    public Poll() {
    }

    public Poll(Integer id) {
        this.id = id;
    }

    public Poll(Integer id, boolean selected, String question, String firstAnswer, String secondAnswer, String thirdAnswer, String fourthAnswer) {
        this.id = id;
        this.selected = selected;
        this.question = question;
        this.firstAnswer = firstAnswer;
        this.secondAnswer = secondAnswer;
        this.thirdAnswer = thirdAnswer;
        this.fourthAnswer = fourthAnswer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(String firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    public String getThirdAnswer() {
        return thirdAnswer;
    }

    public void setThirdAnswer(String thirdAnswer) {
        this.thirdAnswer = thirdAnswer;
    }

    public String getFourthAnswer() {
        return fourthAnswer;
    }

    public void setFourthAnswer(String fourthAnswer) {
        this.fourthAnswer = fourthAnswer;
    }

    public Integer getFirstCount() {
        return firstCount;
    }

    public void setFirstCount(Integer firstCount) {
        this.firstCount = firstCount;
    }

    public Integer getSecondCount() {
        return secondCount;
    }

    public void setSecondCount(Integer secondCount) {
        this.secondCount = secondCount;
    }

    public Integer getThirdCount() {
        return thirdCount;
    }

    public void setThirdCount(Integer thirdCount) {
        this.thirdCount = thirdCount;
    }

    public Integer getFourthCount() {
        return fourthCount;
    }

    public void setFourthCount(Integer fourthCount) {
        this.fourthCount = fourthCount;
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
        if (!(object instanceof Poll)) {
            return false;
        }
        Poll other = (Poll) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g4w16.entities.Poll[ id=" + id + " ]";
    }
    
}
