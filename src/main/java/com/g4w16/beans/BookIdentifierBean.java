package com.g4w16.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("bookIdentifier")
@RequestScoped
public class BookIdentifierBean {
    private String type; //ISBN-13, ISBN-10, ASIN, etc
    private String code;
    
    /**
     * Default constructor for creating an empty book identifier bean
     */
    public BookIdentifierBean(){
        this("", "");
    }
    
    /**
     * Constructor for creating a book identifier bean and setting the fields
     * @param type
     * @param code 
     */
    public BookIdentifierBean(final String type, final String code){
        super();
        this.type = type;
        this.code = code;
    }

    /**
     * Gets the type
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the code
     * 
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     * 
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}
