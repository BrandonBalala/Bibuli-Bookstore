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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
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
        System.out.println("initializing");

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
        System.out.println("Get TotalSales Size 1: " + totalSales.size());
        totalSales = salesController.getTotalSales(startDate, endDate);
        System.out.println("Get TotalSales Size 2: " + totalSales.size());
        return totalSales;
    }

    public void setTotalSales(List<Object[]> totalSales) {
        System.out.println("Setting TotalSales Size Old: " + this.totalSales.size());
        System.out.println("Setting TotalSales Size New: " + totalSales.size());
        this.totalSales = totalSales;
    }

    public List<Object[]> getSalesByClient() {
        System.out.println("Get salesByClient Size 1: " + salesByClient.size());
        salesByClient = salesController.getSalesByClient(startDate, endDate, Integer.valueOf(clientId));
        System.out.println("Get salesByClient Size 2: " + salesByClient.size());
        return salesByClient;
    }

    public void setSalesByClient(List<Object[]> salesByClient) {
        System.out.println("Setting salesByClient Size Old: " + this.salesByClient.size());
        System.out.println("Setting salesByClient Size New: " + salesByClient.size());
        this.salesByClient = salesByClient;
    }

    public List<Object[]> getSalesByContributor() {
        System.out.println("Get salesByContributor Size 1: " + salesByContributor.size());
        salesByContributor = salesController.getSalesByContributor(startDate, endDate, contributorName);
        System.out.println("Get salesByContributor Size 2: " + salesByContributor.size());
        return salesByContributor;
    }

    public void setSalesByContributor(List<Object[]> salesByContributor) {
        System.out.println("Setting salesByContributor Size Old: " + this.salesByContributor.size());
        System.out.println("Setting salesByContributor Size New: " + salesByContributor.size());
        this.salesByContributor = salesByContributor;
    }

    public List<Object[]> getSalesByPublisher() {
        System.out.println("Get salesByPublisher Size 1: " + salesByPublisher.size());
        salesByPublisher = salesController.getSalesByPublisher(startDate, endDate, publisherName);
        System.out.println("Get salesByPublisher Size 2: " + salesByPublisher.size());
        return salesByPublisher;
    }

    public void setSalesByPublisher(List<Object[]> salesByPublisher) {
        System.out.println("Setting salesByPublisher Size Old: " + this.salesByPublisher.size());
        System.out.println("Setting salesByPublisher Size New: " + salesByPublisher.size());
        this.salesByPublisher = salesByPublisher;
    }

    public List<Object[]> getTopSellers() {
        System.out.println("Get topSellers Size 1: " + topSellers.size());
        topSellers = salesController.getTopSellers(startDate, endDate);
        System.out.println("Get topSellers Size 2: " + topSellers.size());
        return topSellers;
    }

    public void setTopSellers(List<Object[]> topSellers) {
        System.out.println("Setting topSellers Size Old: " + this.topSellers.size());
        System.out.println("Setting topSellers Size New: " + topSellers.size());
        this.topSellers = topSellers;
    }

    public List<Object[]> getTopClients() {
        System.out.println("Get topClients Size 1: " + topClients.size());
        topClients = salesController.getTopClients(startDate, endDate);
        System.out.println("Get topClients Size 2: " + topClients.size());
        return topClients;
    }

    public void setTopClients(List<Object[]> topClients) {
        System.out.println("Setting topClients Size Old: " + this.topClients.size());
        System.out.println("Setting topClients Size New: " + topClients.size());
        this.topClients = topClients;
    }

    public List<Object[]> getZeroSales() {
        System.out.println("Get zeroSales Size 1: " + zeroSales.size());
        zeroSales = salesController.getZeroSales(startDate, endDate);
        System.out.println("Get zeroSales Size 2: " + zeroSales.size());
        return zeroSales;
    }

    public void setZeroSales(List<Object[]> topClients) {
        System.out.println("Setting zeroSales Size Old: " + this.zeroSales.size());
        System.out.println("Setting zeroSales Size New: " + zeroSales.size());
        this.zeroSales = zeroSales;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getContributorName(){
        return contributorName;
    }
    
    public void setContributorName(String contributorName){
        this.contributorName = contributorName;
    }
    
    public String getPublisherName(){
        return publisherName;
    }
    
    public void setPublisherName(String publisherName){
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
