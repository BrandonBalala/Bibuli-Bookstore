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
import com.g4w16.entities.IdentifierType;
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
import com.g4w16.persistence.IdentifierTypeJpaController;
import com.g4w16.persistence.ProvinceJpaController;
import com.g4w16.persistence.TaxeRatesJpaController;
import com.g4w16.persistence.TitleJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;

import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Inject
    private IdentifierTypeJpaController identifierTypeJpaController;

    private String newIdentifierType;
    private List<IdentifierType> allIdentifierType;
    private List<IdentifierType> selectedIdentifierType;

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

        allIdentifierType = identifierTypeJpaController.findAllIdentifierTypes();
        selectedIdentifierType = new ArrayList<>();
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

    public void addGenre() {
        boolean noErrors = true;
        if (newGenre.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Genre cannot be longer than 128 characters", "Genre cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Genre genre : allGenre) {
            if (genre.getType().equalsIgnoreCase(newGenre)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate genre", "Cannot have a duplicate genre"));
                noErrors = false;
                break;
            }
        }

        if (noErrors) {
            try {
                Genre g = new Genre();
                g.setType(newGenre);
                genreJpaController.create(g);
                init();
                newGenre = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (RollbackFailureException rfe) {
                newGenre = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, rfe.getMessage(), rfe.getMessage()));
            } catch (Exception e) {
                newGenre = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteGenre(List<Genre> selected) {
        try {
            for (Genre g : selected) {
                genreJpaController.destroy(g.getType());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        boolean noErrors = true;
        if (newContributionType.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contribution type cannot be longer than 128 characters", "Contribution type cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (ContributionType type : allContributionType) {
            if (type.getType().equalsIgnoreCase(newContributionType)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate contribution type", "Cannot have a duplicate contribution type"));
                noErrors = false;
                break;
            }
        }
        if (noErrors) {
            try {
                ContributionType c = new ContributionType();
                c.setType(newContributionType);
                contributionTypeJpaController.create(c);
                init();
                newContributionType = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));

            } catch (Exception e) {
                newContributionType = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteContributionType(List<ContributionType> selected) {
        try {
            for (ContributionType c : selected) {
                contributionTypeJpaController.destroy(c.getType());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        boolean noErrors = true;
        if (newProvince.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Province cannot be longer than 128 characters", "Province cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Province province : allProvince) {
            if (province.getId().equalsIgnoreCase(newProvince)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate province", "Cannot have a duplicate province"));
                noErrors = false;
                break;
            }
        }
        if (noErrors) {
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));

            } catch (Exception e) {
                newProvince = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteProvince(List<Province> selected) {
        try {
            for (Province p : selected) {
                provinceJpaController.destroy(p.getId());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        boolean noErrors = true;
        if (newTitle.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Title cannot be longer than 128 characters", "Title cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Title title : allTitle) {
            if (title.getId().equalsIgnoreCase(newTitle)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate title", "Cannot have a duplicate title"));
                noErrors = false;
                break;
            }
        }

        if (noErrors) {
            try {
                Title t = new Title();
                t.setId(newTitle);
                titleJpaController.create(t);
                init();
                newTitle = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception e) {
                newTitle = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteTitle(List<Title> selected) {
        try {
            for (Title t : selected) {
                titleJpaController.destroy(t.getId());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        boolean noErrors = true;
        if (newFormat.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Format cannot be longer than 128 characters", "Format cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Format format : allFormat) {
            if (format.getType().equalsIgnoreCase(newFormat)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate format", "Cannot have a duplicate format"));
                noErrors = false;
                break;
            }
        }
        if (noErrors) {
            try {
                Format f = new Format();
                f.setType(newFormat);
                formatController.create(f);
                init();
                newFormat = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception e) {
                newFormat = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteFormat(List<Format> selected) {
        try {
            for (Format f : selected) {
                formatController.destroy(f.getType());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        boolean noErrors = true;
        if (newContributor.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contributor cannot be longer than 128 characters", "Contributor cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (Contributor contributor : allContributor) {
            if (contributor.getName().equalsIgnoreCase(newContributor) && contributor.getContribution().getType().equalsIgnoreCase(newType)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate contributor with the same contribution type", "Cannot have a duplicate contributor with the same contribution type"));
                noErrors = false;
                break;
            }
        }

        if (noErrors) {
            try {
                Contributor c = new Contributor();
                c.setName(newContributor);
                c.setContribution(new ContributionType(newType));
                contributorJpaController.create(c);
                init();
                newContributor = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception e) {
                newContributor = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteContributor(List<Contributor> selected) {
        try {
            for (Contributor c : selected) {
                contributorJpaController.destroy(c.getId());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ***************Identifier Type************************
     */
    public String getNewIdentifierType() {
        return newIdentifierType;
    }

    public void setNewIdentifierType(String newIdentifierType) {
        this.newIdentifierType = newIdentifierType;
    }

    public List<IdentifierType> getAllIdentifierType() {
        return allIdentifierType;
    }

    public void setAllIdentifierType(List<IdentifierType> allIdentifierType) {
        this.allIdentifierType = allIdentifierType;
    }

    public List<IdentifierType> getSelectedIdentifierType() {
        return selectedIdentifierType;
    }

    public void setSelectedIdentifierType(List<IdentifierType> selectedIdentifierType) {
        this.selectedIdentifierType = selectedIdentifierType;
    }

    public void addIdentifierType() {
        boolean noErrors = true;
        if (newIdentifierType.length() > 128) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Identifier type cannot be longer than 128 characters", "Identifier type cannot be longer than 128 characters"));
            noErrors = false;
        }
        for (IdentifierType type : allIdentifierType) {
            if (type.getType().equalsIgnoreCase(newIdentifierType)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot have a duplicate identifier type", "Cannot have a duplicate identifer type"));
                noErrors = false;
                break;
            }
        }

        if (noErrors) {
            try {
                IdentifierType i = new IdentifierType();
                i.setType(newIdentifierType);
                identifierTypeJpaController.create(i);
                init();
                newIdentifierType = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Create succesfully!", "Create succesfully!"));
            } catch (Exception e) {
                newIdentifierType = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        }
    }

    public void deleteIdentifierType(List<IdentifierType> selected) {
        try {
            for (IdentifierType i : selected) {
                identifierTypeJpaController.destroy(i.getType());
            }
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete succesfully!", "Delete succesfully!"));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AdminMiscellBackingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
