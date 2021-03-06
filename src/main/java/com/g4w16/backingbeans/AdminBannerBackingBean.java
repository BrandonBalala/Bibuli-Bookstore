/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.Banner;
import com.g4w16.persistence.BannerJpaController;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author wangdan
 */
@Named("bannerBB")
@RequestScoped
public class AdminBannerBackingBean implements Serializable {

    private UploadedFile uploadedFile;
    private List<Banner> banners;
    private List<Integer> ids;
    private int bannerId;
    private List<Banner> filteredBanners;
    private Banner selected;
    private String uri;

    @Inject
    BannerJpaController bannerJpaController;

    @PostConstruct
    public void init() {
        banners = bannerJpaController.findBannerEntities();

    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public int getBannerCount() {
        return bannerJpaController.getBannerCount();
    }

    public Banner getSelected() {
        if (selected == null) {
            selected = new Banner();
        }
        return selected;
    }

    public void setSelected(Banner selected) {
        this.selected = selected;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Integer> getIds() {
        ids = new ArrayList<>();
        for (int i = 0; i < banners.size(); i++) {
            ids.add(i + 1);
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

    public void onRowEdit(RowEditEvent event) {

        try {
            bannerJpaController.edit((Banner) event.getObject());
        } catch (RollbackFailureException ex) {
            Logger.getLogger(AdminBannerBackingBean.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(AdminBannerBackingBean.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void changeStatus(Banner b) throws RollbackFailureException, Exception {
        selected = bannerJpaController.findBanner(b.getId());
        selected.setSelected(b.getSelected());
        bannerJpaController.edit(selected);
    }

    public void addAction(String name) throws Exception {
//        InputStream stream = uploadedFile.getInputstream();
//        Files.copy(stream, new File("/Images/ads/" + name, name).toPath());
//        stream.close();
        
        Banner b = new Banner();
        b.setUri(name);
        b.setSelected(false);
        bannerJpaController.create(b);
        init();
        uri="";
    }
}
