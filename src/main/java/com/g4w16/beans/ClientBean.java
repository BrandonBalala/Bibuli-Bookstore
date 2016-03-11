package com.g4w16.beans;

import java.util.ArrayList;

public class ClientBean {

    private int id;
    private int clientNumber;
    private String title;
    private String lastName;
    private String firstName;
    private String companyName;
    private ArrayList<AddressBean> addresses;
    private String homePhoneNumber;
    private String cellPhoneNumber;
    private String email;

    /**
     * Default constructor for creating an empty client bean
     */
    public ClientBean() {
        this(0, 0, "", "", "", "", new ArrayList<AddressBean>(), "", "", "");
    }

    /**
     * Constructor for creating a client bean and setting its fields
     *
     * @param id
     * @param clientNumber
     * @param title
     * @param lastName
     * @param firstName
     * @param companyName
     * @param addresses
     * @param homePhoneNumber
     * @param cellPhoneNumber
     * @param email
     */
    public ClientBean(final int id, final int clientNumber, final String title, final String lastName, final String firstName,
            final String companyName, final ArrayList<AddressBean> addresses, final String homePhoneNumber, 
            final String cellPhoneNumber, final String email) {
        super();
        this.id = id;
        this.clientNumber = clientNumber;
        this.title = title;
        this.lastName = lastName;
        this.firstName = firstName;
        this.companyName = companyName;
        this.addresses = addresses;
        this.addresses = addresses;
        this.homePhoneNumber = homePhoneNumber;
        this.cellPhoneNumber = cellPhoneNumber;
        this.email = email;
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
     * Gets the client number
     * 
     * @return the clientNumber
     */
    public final int getClientNumber() {
        return clientNumber;
    }

    /**
     * Sets the client number
     * 
     * @param clientNumber the clientNumber to set
     */
    public final void setClientNumber(final int clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     * Gets the title
     * 
     * @return the title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * 
     * @param title the title to set
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the last name
     * 
     * @return the lastName
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     * 
     * @param lastName the lastName to set
     */
    public final void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the first name
     * 
     * @return the firstName
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     * 
     * @param firstName the firstName to set
     */
    public final void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the company name
     * 
     * @return the companyName
     */
    public final String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name
     * 
     * @param companyName the companyName to set
     */
    public final void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the addresses
     * 
     * @return the addresses
     */
    public final ArrayList<AddressBean> getAddresses() {
        return addresses;
    }

    /**
     * Sets the addresses
     * 
     * @param addresses the addresses to set
     */
    public final void setAddresses(final ArrayList<AddressBean> addresses) {
        this.addresses = addresses;
    }
    
    /**
     * Adds an address to the list
     * 
     * @param address the address to add to the list
     */
    public final void addAddress(final AddressBean address) {
        this.addresses.add(address);
    }

    /**
     * Gets the home phone number
     * 
     * @return the homePhoneNumber
     */
    public final String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    /**
     * Sets the home phone number
     * 
     * @param homePhoneNumber the homePhoneNumber to set
     */
    public final void setHomePhoneNumber(final String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    /**
     * Gets the cell phone number
     * 
     * @return the cellPhoneNumber
     */
    public final String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    /**
     * Sets the cell phone number
     * 
     * @param cellPhoneNumber the cellPhoneNumber to set
     */
    public final void setCellPhoneNumber(final String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    /**
     * Gets the email
     * 
     * @return the email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * 
     * @param email the email to set
     */
    public final void setEmail(final String email) {
        this.email = email;
    }
}
