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
import java.util.ArrayList;
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

    private List<BillingAddress> clientAddressList;
    private BillingAddress choiceAddress;
    private Client client;

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

    public BillingAddress getChoiceAddress() {
        return choiceAddress;
    }

    public void setChoiceAddress(BillingAddress choiceAddress) {
        this.choiceAddress = choiceAddress;
    }

    public List<BillingAddress> getClientAddressList() {
        return clientAddressList;
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

    public List<Province> getProvinceList() {
        return provinceController.findProvinceEntities();
    }

    public String createNewBillingAddress() throws Exception {
        choiceAddress.setClient(client);
        choiceAddress.setId(client.getId());
        billingController.create(choiceAddress);
        return "payment";
    }

    public String displayCheckoutPage() {
        if (!loginBB.isLoggedIn()) {
            return "login";
        }

        List<Client> clientList = clientController.findClientByEmail(loginBB.getEmail());

        if (clientList == null || clientList.isEmpty()) {
            return "login";
        }

        client = clientList.get(0);

        List<BillingAddress> tempAddressList = client.getBillingAddressList();

        if (tempAddressList == null) {
            tempAddressList = new ArrayList<BillingAddress>();
        }

        clientAddressList = tempAddressList;

        return "shipping-address";
    }

    public String proceedToPayment(BillingAddress address) {
        choiceAddress = address;

        return "payment";
    }

    public BigDecimal calculatePST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getPst().divide(BigDecimal.valueOf(100));

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        //Calculate PST & Round to 2 decimal points
        BigDecimal pst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return pst;
    }

    public BigDecimal calculateHST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getHst().divide(BigDecimal.valueOf(100));

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        //Calculate HST & Round to 2 decimal points
        BigDecimal hst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return hst;
    }

    public BigDecimal calculateQST() {
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        BigDecimal subTotal = cartBB.getSubtotal();
        BigDecimal rate = tax.getQst().divide(BigDecimal.valueOf(100));

        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        //Calculate QST & Round to 2 decimal points
        BigDecimal qst = subTotal.multiply(rate).setScale(2, RoundingMode.CEILING);
        return qst;
    }

    public BigDecimal calculateOrderTotal() {
        BigDecimal subtotal = cartBB.getSubtotal();
        BigDecimal pst = calculatePST();
        BigDecimal hst = calculateHST();
        BigDecimal qst = calculateQST();
        BigDecimal total = subtotal.add(pst).add(hst).add(qst);

        return total;
    }
    
    public String placeOrder() throws Exception{
        Sales sale = new Sales();
        sale.setClient(client);
        sale.setNetValue(cartBB.getSubtotal());
        sale.setGrossValue(calculateOrderTotal());
        salesController.create(sale);
        
        TaxeRates tax = taxeRatesController.findTaxeRates(choiceAddress.getProvince());
        
        for (Books book : cartBB.getBookList()) {
            SalesDetails saleDetail = new SalesDetails();
            saleDetail.setBook(book);
            saleDetail.setSale(sale);
            saleDetail.setPst(tax.getPst());
            saleDetail.setHst(tax.getHst());
            saleDetail.setQst(tax.getQst());
            
            if(book.getSalePrice().equals(BigDecimal.ZERO))
                saleDetail.setPrice(book.getListPrice());
            else
                saleDetail.setPrice(book.getSalePrice());
            
            salesDetailController.create(saleDetail);
        }
        
        return null;
    }

    @PostConstruct
    public void init() {
        choiceAddress = new BillingAddress();
    }
}
