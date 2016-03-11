package com.g4w16.beans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Annie So
 */
public class InvoiceBean {

    private int saleNumber;
    private LocalDate saleDate;
    private int clientNumber;
    private ArrayList<InvoiceDetailBean> invoiceDetails;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private boolean removed;

    /**
     * Default constructor for creating an empty invoice bean.
     */
    public InvoiceBean() {
        this(0, null, 0, new ArrayList<InvoiceDetailBean>(), new BigDecimal(0),
                new BigDecimal(0), false);
    }

    /**
     * Constructor for creating a new invoice bean and setting each field of the
     * bean.
     *
     * @param saleNumber The sale number.
     * @param saleDate The date of the sale.
     * @param clientNumber The client number of the client who made the
     * purchase.
     * @param invoiceDetails An array list of the invoice details that make up
     * this invoice.
     * @param netValue The total net value of the sale.
     * @param grossValue The total gross value of the sale.
     * @param removed The removal status of the invoice.
     */
    public InvoiceBean(final int saleNumber, final LocalDate saleDate, final int clientNumber,
            final ArrayList<InvoiceDetailBean> invoiceDetails, final BigDecimal netValue,
            final BigDecimal grossValue, final boolean removed) {
        super();
        this.saleNumber = saleNumber;
        this.saleDate = saleDate;
        this.clientNumber = clientNumber;
        this.invoiceDetails = invoiceDetails;
        this.netValue = netValue;
        this.grossValue = grossValue;
        this.removed = removed;
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
     * Gets the sale date.
     *
     * @return The sale date.
     */
    public LocalDate getSaleDate() {
        return saleDate;
    }

    /**
     * Sets the sale date.
     *
     * @param saleDate The sale date to set.
     */
    public void setSaleDate(final LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    /**
     * Gets the client number.
     *
     * @return The client number.
     */
    public int getClientNumber() {
        return clientNumber;
    }

    /**
     * Sets the client number.
     *
     * @param clientNumber The client number to set.
     */
    public void setClientNumber(final int clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     * Gets an array list of invoice detail beans for this invoice.
     *
     * @return An array list of invoice details.
     */
    public ArrayList<InvoiceDetailBean> getInvoiceDetails() {
        return invoiceDetails;
    }

    /**
     * Gets the net value of the sale.
     *
     * @return The net value.
     */
    public BigDecimal getNetValue() {
        return netValue;
    }

    /**
     * Sets the net value of the sale.
     *
     * @param netValue The net value to set.
     */
    public void setNetValue(final BigDecimal netValue) {
        this.netValue = netValue;
    }

    /**
     * Gets the gross value of the sale.
     *
     * @return The gross value.
     */
    public BigDecimal getGrossValue() {
        return grossValue;
    }

    /**
     * Sets the gross value of the sale.
     *
     * @param grossValue The gross value to set.
     */
    public void setGrossValue(final BigDecimal grossValue) {
        this.grossValue = grossValue;
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
