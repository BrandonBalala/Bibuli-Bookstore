package com.g4w16.beans;

import java.math.BigDecimal;

/**
 *
 * @author Annie So
 */
public class InvoiceDetailBean {

    private int id;
    private int saleNumber;
    private String isbn;
    private BigDecimal price;
    private BigDecimal pst;
    private BigDecimal gst;
    private BigDecimal hst;
    private boolean removed;

    /**
     * Default constructor for creating an empty invoice details bean.
     */
    public InvoiceDetailBean() {
        this(0, 0, "", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
                new BigDecimal(0), false);
    }

    /**
     * Constructor for creating a new invoice details bean and setting each
     * field of the bean.
     *
     * @param id The id of the invoice detail.
     * @param saleNumber The sale number this invoice detail belongs to.
     * @param isbn The ISBN of the book that was sold.
     * @param price The price of the book that was sold.
     * @param pst The PST for the book.
     * @param gst The GST for the book.
     * @param hst The HST for the book.
     * @param removed The removal status of the invoice detail.
     */
    public InvoiceDetailBean(final int id, final int saleNumber, final String isbn,
            final BigDecimal price, final BigDecimal pst, final BigDecimal gst,
            final BigDecimal hst, final boolean removed) {
        super();
        this.id = id;
        this.saleNumber = saleNumber;
        this.isbn = isbn;
        this.price = price;
        this.pst = pst;
        this.gst = gst;
        this.hst = hst;
        this.removed = removed;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The id to set.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the sale number.
     *
     * @return The sale number.
     */
    public int getSaleNumber() {
        return saleNumber;
    }

    /**
     * Sets the sale number.
     *
     * @param saleNumber The sale number to set.
     */
    public void setSaleNumber(final int saleNumber) {
        this.saleNumber = saleNumber;
    }

    /**
     * Gets the ISBN of the book sold.
     *
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book sold.
     *
     * @param isbn The ISBN to set.
     */
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the price of the book sold.
     *
     * @return The price.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the book sold.
     *
     * @param price The price to set.
     */
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the PST calculated for the book.
     *
     * @return The PST.
     */
    public BigDecimal getPst() {
        return pst;
    }

    /**
     * Sets the PST calculated for the book.
     *
     * @param pst The PST to set.
     */
    public void setPst(final BigDecimal pst) {
        this.pst = pst;
    }

    /**
     * Gets the GST calculated for the book.
     *
     * @return The GST.
     */
    public BigDecimal getGst() {
        return gst;
    }

    /**
     * Sets the GST calculated for the book.
     *
     * @param gst The GST to set.
     */
    public void setGst(final BigDecimal gst) {
        this.gst = gst;
    }

    /**
     * Gets the HST calculated for the book.
     *
     * @return The HST.
     */
    public BigDecimal getHst() {
        return hst;
    }

    /**
     * Sets the HST calculated for the book.
     *
     * @param hst The HST to set.
     */
    public void setHst(final BigDecimal hst) {
        this.hst = hst;
    }

    /**
     * Gets the removal status.
     *
     * @return The removal status.
     */
    public boolean getRemoved() {
        return removed;
    }

    /**
     * Sets the removal status.
     *
     * @param removed The removal status to set.
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
