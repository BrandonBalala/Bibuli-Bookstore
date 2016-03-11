package com.g4w16.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author BRANDON-PC
 */
@Named("bookFormat")
@RequestScoped
public class BookFormatBean {

    private String format;
    private String file; //file name

    /**
     * Default constructor for creating an empty book format bean and setting its
     * fields
     */
    public BookFormatBean() {
        this("", "");
    }

    /**
     * Constructor for creating a book format bean and setting the fields
     * @param format
     * @param file 
     */
    public BookFormatBean(final String format, final String file) {
        super();
        this.format = format;
        this.file = file;
    }

    /**
     * Gets the format
     *
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format
     *
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets the file
     *
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the file
     *
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }
}
