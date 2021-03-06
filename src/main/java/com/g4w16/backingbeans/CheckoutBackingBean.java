package com.g4w16.backingbeans;

import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
import com.g4w16.entities.Province;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.entities.TaxeRates;
import com.g4w16.persistence.BillingAddressJpaController;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.ProvinceJpaController;
import com.g4w16.persistence.SalesDetailsJpaController;
import com.g4w16.persistence.SalesJpaController;
import com.g4w16.persistence.TaxeRatesJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Brandon Balala
 */
@Named("checkoutBB")
@SessionScoped
public class CheckoutBackingBean implements Serializable {

    private BillingAddress choiceAddress;
    private BillingAddress newAddress;

    private String cardNumber;
    private String nameOnCard;
    private String expiryMonth;
    private String expiryYear;
    private String securityCode;

    @Inject
    private LoginBackingBean loginBB;

    @Inject
    private ShoppingCartBackingBean cartBB;

    @Inject
    private ClientJpaController clientController;

    @Inject
    private ProvinceJpaController provinceController;

    @Inject
    private BillingAddressJpaController billingController;

    @Inject
    private TaxeRatesJpaController taxeRatesController;

    @Inject
    private SalesJpaController salesController;

    @Inject
    private SalesDetailsJpaController salesDetailController;

    @Inject
    private ClientUtil clientUtil;

    @Inject
    private InvoiceBackingBean invoiceBB;

    /**
     * Get choice address
     * @return choiceAddress
     */
    public BillingAddress getChoiceAddress() {
        return choiceAddress;
    }

    /**
     * Set choice address
     * @param choiceAddress 
     */
    public void setChoiceAddress(BillingAddress choiceAddress) {
        this.choiceAddress = choiceAddress;
    }

    /**
     * Get new address
     * @return newAddress
     */
    public BillingAddress getNewAddress() {
        return newAddress;
    }

    /**
     * Set new address
     * @param newAddress 
     */
    public void setNewAddress(BillingAddress newAddress) {
        this.newAddress = newAddress;
    }

    /**
     * Get client address list
     * @return List<BillingAddress>
     */
    public List<BillingAddress> getClientAddressList() {

        Client client = clientController.findClientById(clientUtil.getUserId());

        if (client == null) {
            return new ArrayList<BillingAddress>();
        }

        return client.getBillingAddressList();
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the nameOnCard
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /**
     * @param nameOnCard the nameOnCard to set
     */
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    /**
     * @return the expiryMonth
     */
    public String getExpiryMonth() {
        return expiryMonth;
    }

    /**
     * @param expiryMonth the expiryMonth to set
     */
    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    /**
     * @return the expiryYear
     */
    public String getExpiryYear() {
        return expiryYear;
    }

    /**
     * @param expiryYear the expiryYear to set
     */
    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    /**
     * @return the securityCode
     */
    public String getSecurityCode() {
        return securityCode;
    }

    /**
     * @param securityCode the securityCode to set
     */
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    /**
     * Get province list
     * @return List<Province>
     */
    public List<Province> getProvinceList() {
        return provinceController.findProvinceEntities();
    }

    /**
     * Create new billing address
     * @throws Exception 
     */
    public void createNewBillingAddress() throws Exception {
        newAddress.setClient(clientController.findClientById(clientUtil.getUserId()));
        newAddress.setId(clientUtil.getUserId());
        billingController.create(newAddress);
        clearAddressInfo();

        //return "payment";
    }

    /**
     * Display checkout page
     * @return String
     */
    public String displayCheckoutPage() {
        if (!validateAuthenticated()) {
            return "login";
        }

        return "shipping-address";
    }

    /**
     * Proceed to the payment page
     * @param address
     * @return String
     */
    public String proceedToPayment(BillingAddress address) {
        if (!validateAuthenticated()) {
            return "login";
        }

        choiceAddress = address;

        return "payment";
    }

    /**
     * Calculate the PST
     * @return BigDecimal
     */
    public BigDecimal calculatePST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getPst();

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        //Calculate PST & Round to 2 decimal points
        BigDecimal pst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return pst;
    }

    /**
     * Calculate the HST
     * @return BigDecimal
     */
    public BigDecimal calculateHST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getHst();

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        //Calculate HST & Round to 2 decimal points
        BigDecimal hst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return hst;
    }

    /**
     * Calculate the GST
     * @return BigDecimal
     */
    public BigDecimal calculateGST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getGst();

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        //Calculate GST & Round to 2 decimal points
        BigDecimal qst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return qst;
    }

    /**
     * Calculate order total
     * @return BigDecimal 
     */
    public BigDecimal calculateOrderTotal() {
        BigDecimal subtotal = cartBB.getSubtotal();
        BigDecimal pst = calculatePST();
        BigDecimal hst = calculateHST();
        BigDecimal qst = calculateGST();
        BigDecimal total = subtotal.add(pst).add(hst).add(qst);

        return total;
    }

    /**
     * Place the order, redirect to right page
     * @return
     * @throws Exception 
     */
    public String placeOrder() throws Exception {
        if (!validateAuthenticated()) {
            return "login";
        }

        Sales sale = new Sales();
        sale.setDateEntered(Date.from(Instant.now()));
        sale.setClient(clientController.findClientById(clientUtil.getUserId()));
        sale.setNetValue(cartBB.getSubtotal());
        sale.setGrossValue(calculateOrderTotal());
        sale.setBillingAddress(choiceAddress);
        sale.setRemoved(false);

        salesController.create(sale);

        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());

        for (Books book : cartBB.getBookList()) {
            SalesDetails saleDetail = new SalesDetails();
            saleDetail.setBook(book);
            saleDetail.setSale(sale);
            saleDetail.setPst(tax.getPst());
            saleDetail.setHst(tax.getHst());
            saleDetail.setGst(tax.getGst());
            saleDetail.setRemoved(false);

            if (book.getSalePrice().equals(BigDecimal.ZERO)) {
                saleDetail.setPrice(book.getListPrice());
            } else {
                saleDetail.setPrice(book.getSalePrice());
            }

            salesDetailController.create(saleDetail);
        }

        clearCreditCardInfo();
        clearCart();

        return displayInvoice(salesController.findSales(sale.getId()));
    }
    
    /**
     * Get minimum year
     * @return int
     */
    public int getMinYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    /**
     * Get maximum year
     * @return 
     */
    public int getMaxYear(){
        return (Calendar.getInstance().get(Calendar.YEAR) + 5);
    }

    /**
     * Display invoice for the sale
     * @param sale
     * @return String
     */
    private String displayInvoice(Sales sale) {
        invoiceBB.setSale(sale);
        return "invoice";
    }

    /**
     * Clear the credit card information
     */
    private void clearCreditCardInfo() {
        cardNumber = "";
        nameOnCard = "";
        expiryMonth = "";
        expiryYear = "";
        securityCode = "";
    }

    /**
     * Clear address information
     */
    private void clearAddressInfo() {
        newAddress = new BillingAddress();
    }

    /**
     * Clear the cart
     */
    private void clearCart() {
        cartBB.getBookList().clear();
    }

    /**
     * Validate that client is authenticated
     * @return 
     */
    private boolean validateAuthenticated() {
        try {
            if (!clientUtil.isAuthenticated()) {
                return false;
            }

            int clientID = clientUtil.getUserId();
            Client client = clientController.findClientById(clientID);

            if (client == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @PostConstruct
    public void init() {
        choiceAddress = new BillingAddress();
        newAddress = new BillingAddress();
    }
}
