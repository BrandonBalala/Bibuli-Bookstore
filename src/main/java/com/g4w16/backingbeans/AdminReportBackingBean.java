/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.persistence.SalesJpaController;
import java.io.Serializable;
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

        startDate = new Date();
        endDate = new Date();
    }

    public List<Object[]> getTotalSales() {
        totalSales = salesController.getTotalSales(startDate, endDate);
        return totalSales;
    }

    public void setTotalSales(List<Object[]> totalSales) {
        this.totalSales = totalSales;
    }

    public List<Object[]> getSalesByClient() {
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        return salesByClient;
    }

    public void setSalesByClient(List<Object[]> salesByClient) {
        this.salesByClient = salesByClient;
    }

    public List<Object[]> getSalesByContributor() {
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        return salesByContributor;
    }

    public void setSalesByContributor(List<Object[]> salesByContributor) {
        this.salesByContributor = salesByContributor;
    }

    public List<Object[]> getSalesByPublisher() {
        salesByPublisher = salesController.getSalesByPublisher(startDate, endDate, publisherName);
        return salesByPublisher;
    }

    public void setSalesByPublisher(List<Object[]> salesByPublisher) {
        this.salesByPublisher = salesByPublisher;
    }

    public List<Object[]> getTopSellers() {
        topSellers = salesController.getTopSellers(startDate, endDate);
        return topSellers;
    }

    public void setTopSellers(List<Object[]> topSellers) {
        this.topSellers = topSellers;
    }

    public List<Object[]> getTopClients() {
        topClients = salesController.getTopClients(startDate, endDate);
        return topClients;
    }

    public void setTopClients(List<Object[]> topClients) {
         this.topClients = topClients;
    }

    public List<Object[]> getZeroSales() {
        zeroSales = salesController.getZeroSales(startDate, endDate);
        return zeroSales;
    }

    public void setZeroSales(List<Object[]> zeroSales) {
        this.zeroSales = zeroSales;
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
