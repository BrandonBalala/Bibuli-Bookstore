package com.g4w16.beans;

import java.util.ArrayList;

/**
 *
 * @author BRANDON-PC
 */
public class AddressBean {

    private int id;
    private String name;
    private String address1;
    private String address2; //remove if not necessary
    private String city;
    private String province;
    private String country; //Always canada not needed
    private String postalCode;

    /**
     * Default constructor for creating an empty address bean
     */
    public AddressBean() {
        this("", "", "", "", "", "", "");
    }

    /**
     * Constructor for creating an empty address bean and setting its fields
     *
     * @param address1
     * @param address2
     * @param city
     * @param province
     * @param country
     * @param postalCode
     */
    public AddressBean(final String name, final String address1, final String address2, final String city, final String province,
            final String country, final String postalCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.name = name;
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
    public final void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the name associated with this address
     * 
     * @return the name associated with the address
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name for the address
     * 
     * @param name the name to set to the address
     */
    public final void setName(final String name) {
        this.name = name;
    }
    
    /**
     * Gets the address 1
     * 
     * @return the address1
     */
    public final String getAddress1() {
        return address1;
    }

    /**
     * Sets the address 1
     * 
     * @param address1 the address1 to set
     */
    public final void setAddress1(final String address1) {
        this.address1 = address1;
    }

    /**
     * Gets the address 2
     * 
     * @return the address2
     */
    public final String getAddress2() {
        return address2;
    }

    /**
     * Sets the address 2
     * 
     * @param address2 the address2 to set
     */
    public final void setAddress2(final String address2) {
        this.address2 = address2;
    }

    /**
     * Gets the city
     * 
     * @return the city
     */
    public final String getCity() {
        return city;
    }

    /**
     * Sets the city
     * 
     * @param city the city to set
     */
    public final void setCity(final String city) {
        this.city = city;
    }

    /**
     * Gets the province
     * 
     * @return the province
     */
    public final String getProvince() {
        return province;
    }

    /**
     * Sets the province
     * 
     * @param province the province to set
     */
    public final void setProvince(final String province) {
        this.province = province;
    }

    /**
     * Gets the country
     * 
     * @return the country
     */
    public final String getCountry() {
        return country;
    }

    /**
     * Sets the country
     * 
     * @param country the country to set
     */
    public final void setCountry(final String country) {
        this.country = country;
    }

    /**
     * Gets the postal code
     * 
     * @return the postalCode
     */
    public final String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code
     * 
     * @param postalCode the postalCode to set
     */
    public final void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }
}
