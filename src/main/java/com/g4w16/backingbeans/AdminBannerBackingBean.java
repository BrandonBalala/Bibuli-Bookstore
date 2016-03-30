/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Banner;
import com.g4w16.persistence.BannerJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangdan
 */
@Named("bannerBB")
@RequestScoped
public class AdminBannerBackingBean implements Serializable { 
    private List<Banner> banners;
    private List<Integer> ids ;
    private int bannerId;
    private List<Banner> filteredBanners;
    private Banner selected;
    
    @Inject
    BannerJpaController bannerJpaController;
    
    @PostConstruct
    public void init() {
        banners = bannerJpaController.findBannerEntities();
        
    }
    
    public List<Banner> getBanners(){
        return banners;
    }
    
    public int getBannerCount(){
        return bannerJpaController.getBannerCount();
    }

    public Banner getSelected() {
        if(selected==null)
           selected= new Banner();
        return selected;
    }

    public void setSelected(Banner selected) {
        this.selected = selected;
    }
    
    public List<Integer> getIds() {
        ids=new ArrayList<>();
         for(int i=0;i<banners.size();i++){
            ids.add(i+1);
         }
         return ids;
    }
    
    public int getBannerId() {
        return bannerId;
    }
 
    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }
    
    public List<Banner> getFilteredBanners() {
        return filteredBanners;
    }
 
    public void setFilteredBanners(List<Banner> filteredBanners) {
        this.filteredBanners = filteredBanners;
    }
    
    public void onRowEdit(RowEditEvent event) throws RollbackFailureException, Exception {
        bannerJpaController.edit((Banner) event.getObject());
    }
     
    public void onRowCancel(RowEditEvent event) {
    }
    
   public void changeStatus(Banner b) throws RollbackFailureException, Exception{
      selected=bannerJpaController.findBanner(b.getId());
       selected.setSelected(b.getSelected());
       bannerJpaController.edit(selected);
   }
    
}
