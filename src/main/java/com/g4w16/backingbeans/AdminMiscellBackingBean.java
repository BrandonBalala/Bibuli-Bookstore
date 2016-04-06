/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.entities.Province;
import com.g4w16.entities.TaxeRates;
import com.g4w16.entities.Title;
import com.g4w16.persistence.ContributionTypeJpaController;
import com.g4w16.persistence.ContributorJpaController;
import com.g4w16.persistence.FormatJpaController;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.g4w16.persistence.GenreJpaController;
import com.g4w16.persistence.ProvinceJpaController;
import com.g4w16.persistence.TaxeRatesJpaController;
import com.g4w16.persistence.TitleJpaController;

import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Dan
 * @author Annie So
 */
@Named("miscellBB")
@RequestScoped
public class AdminMiscellBackingBean implements Serializable {

    @Inject
    private GenreJpaController genreJpaController;

    private String newGenre;
    private List<Genre> allGenre;
    private List<Genre> selectedGenre;

    @Inject
    private ContributionTypeJpaController contributionTypeJpaController;

    private String newContributionType;
    private List<ContributionType> allContributionType;
    private List<ContributionType> selectedContributionType;

    @Inject
    private ProvinceJpaController provinceJpaController;
    @Inject
    private TaxeRatesJpaController taxController;

    private String newProvince;
    private List<Province> allProvince;
    private List<Province> selectedProvince;

    @Inject
    private TitleJpaController titleJpaController;

    private String newTitle;
    private List<Title> allTitle;
    private List<Title> selectedTitle;

    @Inject
    private FormatJpaController formatController;

    private String newFormat;
    private List<Format> allFormat;
    private List<Format> selectedFormat;

    @Inject
    private ContributorJpaController contributorJpaController;

    private String newContributor;
    private String newType;
    private List<Contributor> allContributor;
    private List<Contributor> selectedContributor;

    @PostConstruct
    public void init() {
        allGenre = genreJpaController.findAllGenres();
        selectedGenre = new ArrayList<>();

        allContributionType = contributionTypeJpaController.findAllContributionTypes();
        selectedContributionType = new ArrayList<>();

        allProvince = provinceJpaController.findProvinceEntities();
        selectedProvince = new ArrayList<>();

        allTitle = titleJpaController.findTitleEntities();
        selectedTitle = new ArrayList<>();

        allFormat = formatController.findAllFormats();
        selectedFormat = new ArrayList<>();

        allContributor = contributorJpaController.findAllContributors();
        selectedContributor = new ArrayList<>();

    }

    /**
     * ************************Genre********************************
     */
    public List<Genre> getAllGenre() {
        return allGenre;
    }

    public List<Genre> getSelectedGenre() {
        return selectedGenre;
    }

    public void setSelectedGenre(List<Genre> selectedGenre) {
        this.selectedGenre = selectedGenre;
    }

    public String getNewGenre() {
        return newGenre;
    }

    public void setNewGenre(String newGenre) {
        this.newGenre = newGenre;
    }

    public void addGenre() throws RollbackFailureException, Exception {
        try {
            Genre g = new Genre();
            g.setType(newGenre);
            genreJpaController.create(g);
            init();
            newGenre = "";
        } catch (RollbackFailureException rfe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, rfe.getMessage(), rfe.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

    public void deleteGenre(List<Genre> selected) throws RollbackFailureException, Exception {
        for (Genre g : selected) {
            genreJpaController.destroy(g.getType());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }

    /**
     * ****************Contribution***********************
     */
    public String getNewContributionType() {
        return newContributionType;
    }

    public void setNewContributionType(String newContributionType) {
        this.newContributionType = newContributionType;
    }

    public List<ContributionType> getAllContributionType() {
        return allContributionType;
    }

    public void setAllContributionType(List<ContributionType> allContributionType) {
        this.allContributionType = allContributionType;
    }

    public List<ContributionType> getSelectedContributionType() {
        return selectedContributionType;
    }

    public void setSelectedContributionType(List<ContributionType> selectedContributionType) {
        this.selectedContributionType = selectedContributionType;
    }

    public void addContributionType() {
        try {
            ContributionType c = new ContributionType();
            c.setType(newContributionType);
            contributionTypeJpaController.create(c);
            init();
            newContributionType = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

    public void deleteContributionType(List<ContributionType> selected) throws RollbackFailureException, Exception {
        for (ContributionType c : selected) {
            contributionTypeJpaController.destroy(c.getType());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }

    /**
     * **************************Provence***********************************
     */
    public String getNewProvince() {
        return newProvince;
    }

    public void setNewProvince(String newProvince) {
        this.newProvince = newProvince;
    }

    public List<Province> getAllProvince() {
        return allProvince;
    }

    public void setAllProvince(List<Province> allProvince) {
        this.allProvince = allProvince;
    }

    public List<Province> getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(List<Province> selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public void addProvince() {
        try {
            Province p = new Province();
            p.setId(newProvince);
            provinceJpaController.create(p);

            TaxeRates tax = new TaxeRates();
            tax.setProvince(newProvince);
            tax.setGst(BigDecimal.ZERO);
            tax.setHst(BigDecimal.ZERO);
            tax.setPst(BigDecimal.ZERO);
            tax.setUpdated(new Date());
            taxController.create(tax);

            init();
            newProvince = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

    public void deleteProvince(List<Province> selected) throws RollbackFailureException, Exception {
        for (Province p : selected) {
            provinceJpaController.destroy(p.getId());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }

    /**
     * ********************Title*******************************
     */
    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public List<Title> getAllTitle() {
        return allTitle;
    }

    public void setAllTitle(List<Title> allTitle) {
        this.allTitle = allTitle;
    }

    public List<Title> getSelectedTitle() {
        return selectedTitle;
    }

    public void setSelectedTitle(List<Title> selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    public void addTitle() {
        try {
            Title t = new Title();
            t.setId(newTitle);
            titleJpaController.create(t);
            init();
            newTitle = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

    public void deleteTitle(List<Title> selected) throws RollbackFailureException, Exception {
        for (Title t : selected) {
            titleJpaController.destroy(t.getId());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }

    /**
     * ********************Format*******************************
     */
    public String getNewFormat() {
        return newFormat;
    }

    public void setNewFormat(String newFormat) {
        this.newFormat = newFormat;
    }

    public List<Format> getAllFormat() {
        return allFormat;
    }

    public void setAllFormat(List<Format> allFormat) {
        this.allFormat = allFormat;
    }

    public List<Format> getSelectedFormat() {
        return selectedFormat;
    }

    public void setSelectedFormat(List<Format> selectedFormat) {
        this.selectedFormat = selectedFormat;
    }

    public void addFormat() {
        try {
            Format f = new Format();
            f.setType(newFormat);
            formatController.create(f);
            init();
            newFormat = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Create succesfully!"));
    }

    public void deleteFormat(List<Format> selected) throws RollbackFailureException, Exception {
        for (Format f : selected) {
            formatController.destroy(f.getType());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Delete succesfully!"));
    }

    /**
     * ****************Contributor*************************
     */
    public String getNewContributor() {
        return newContributor;
    }

    public void setNewContributor(String newContributor) {
        this.newContributor = newContributor;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public List<Contributor> getAllContributor() {
        return allContributor;
    }

    public void setAllContributor(List<Contributor> allContributor) {
        this.allContributor = allContributor;
    }

    public List<Contributor> getSelectedContributor() {
        return selectedContributor;
    }

    public void setSelectedContributor(List<Contributor> selectedContributor) {
        this.selectedContributor = selectedContributor;
    }

    public void addContributor() {
        try {
            Contributor c = new Contributor();
            c.setName(newContributor);
            c.setContribution(new ContributionType(newType));
            contributorJpaController.create(c);
            init();
            newContributor = "";
            newType=null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
    }

    public void deleteContributor(List<Contributor> selected) throws RollbackFailureException, Exception {
        for (Contributor c : selected) {
            contributorJpaController.destroy(c.getId());
        }
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
    }

}
