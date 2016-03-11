package com.g4w16.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author BRANDON-PC
 */
@Named("contributor")
@RequestScoped
public class ContributorBean {

    private int id;
    private String name;
    private String contribution;

    /**
     * Default constructor for creating an empty contributor bean and setting its
     * fields
     */
    public ContributorBean() {
        this(0, "", "");
    }

    /**
     * Constructor for creating a contributor bean
     * @param id
     * @param name
     * @param contribution 
     */
    public ContributorBean(final int id, final String name, final String contribution) {
        super();
        this.id = id;
        this.name = name;
        this.contribution = contribution;
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the name
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the contribution
     *
     * @return the contribution
     */
    public final String getContribution() {
        return contribution;
    }

    /**
     * Sets the contribution
     *
     * @param contribution the contribution to set
     */
    public void setContribution(final String contribution) {
        this.contribution = contribution;
    }

}
