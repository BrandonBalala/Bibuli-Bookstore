/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.persistence.SalesJpaController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Annie So
 * @author wangdan
 */
@Named("reportBB")
@RequestScoped
public class AdminReportBackingBean implements Serializable {

    @Inject
    private SalesJpaController salesController;

    private List<Object[]> totalSales;
    private List<Object[]> salesByClient;
    private List<Object[]> salesByContributor;
    private List<Object[]> salesByPublisher;
    private List<Object[]> topSellers;
    private List<Object[]> topClients;
    private List<Object[]> zeroSales;

    private String clientId;
    private String contributorName;
    private String publisherName;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init() {
        totalSales = new ArrayList<>();
        salesByClient = new ArrayList<>();
        salesByContributor = new ArrayList<>();
        salesByPublisher = new ArrayList<>();
        topSellers = new ArrayList<>();
        topClients = new ArrayList<>();
        zeroSales = new ArrayList<>();

        clientId = "0";
        contributorName = "";
        publisherName = "";

        startDate = new Date();
        endDate = new Date();
    }

    public List<Object[]> getTotalSales() {
        totalSales = salesController.getTotalSales(startDate, endDate);
        return totalSales;
    }

    public BigDecimal getTotalSalesTotalSales() {
        totalSales = salesController.getTotalSales(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : totalSales) {
            total = total.add((BigDecimal) sale[2]);
        }

        return total;
    }

    public BigDecimal getTotalSalesTotalCost() {
        totalSales = salesController.getTotalSales(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : totalSales) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getTotalSalesTotalProfit() {
        totalSales = salesController.getTotalSales(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : totalSales) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public List<Object[]> getSalesByClient() {
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        return salesByClient;
    }

    public BigDecimal getSalesByClientTotalSales() {
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByClient) {
            total = total.add((BigDecimal) sale[2]);
        }

        return total;
    }

    public BigDecimal getSalesByClientTotalCost() {
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByClient) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getSalesByClientTotalProfit() {
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByClient) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public List<Object[]> getSalesByContributor() {
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        return salesByContributor;
    }

    public BigDecimal getSalesByContributorTotalSales() {
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByContributor) {
            total = total.add((BigDecimal) sale[2]);
        }

        return total;
    }

    public BigDecimal getSalesByContributorTotalCost() {
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByContributor) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getSalesByContributorTotalProfit() {
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByClient) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public List<Object[]> getSalesByPublisher() {
        salesByPublisher = salesController.getSalesByPublisher(startDate, endDate, publisherName);
        return salesByPublisher;
    }
    
      public BigDecimal getSalesByPublisherTotalSales() {
        salesByPublisher = salesController.getSalesByPublisher(startDate, endDate, publisherName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByPublisher) {
            total = total.add((BigDecimal) sale[2]);
        }

        return total;
    }

    public BigDecimal getSalesByPublisherTotalCost() {
        salesByPublisher = salesController.getSalesByPublisher(startDate, endDate, publisherName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByPublisher) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getSalesByPublisherTotalProfit() {
        salesByPublisher = salesController.getSalesByContributor(startDate, endDate, publisherName);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : salesByClient) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public List<Object[]> getTopSellers() {
        topSellers = salesController.getTopSellers(startDate, endDate);
        return topSellers;
    }
    
     public BigDecimal getTopSellersTotalSales() {
        topSellers = salesController.getTopSellers(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topSellers) {
            total = total.add((BigDecimal) sale[2]);
        }

        return total;
    }

    public BigDecimal getTopSellersTotalCost() {
        topSellers = salesController.getTopSellers(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topSellers) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getTopSellersTotalProfit() {
        topSellers = salesController.getTopSellers(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topSellers) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public List<Object[]> getTopClients() {
        topClients = salesController.getTopClients(startDate, endDate);
        return topClients;
    }
    
     public BigDecimal getTopClientsTotalSales() {
        topClients = salesController.getTopClients(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topClients) {
            total = total.add((BigDecimal) sale[3]);
        }

        return total;
    }

    public BigDecimal getTopClientsTotalCost() {
        topClients = salesController.getTopClients(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topClients) {
            total = total.add((BigDecimal) sale[4]);
        }

        return total;
    }

    public BigDecimal getTopClientsTotalProfit() {
        topClients = salesController.getTopClients(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;

        for (Object[] sale : topClients) {
            total = total.add((BigDecimal) sale[5]);
        }

        return total;
    }

    public List<Object[]> getZeroSales() {
        zeroSales = salesController.getZeroSales(startDate, endDate);
        return zeroSales;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
