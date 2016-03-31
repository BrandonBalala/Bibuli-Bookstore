/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
 

/**
 *
 * @author wangdan
 */
@Named("reportBB")
@ManagedBean
public class AdminReportBackingBean implements Serializable { 
    
    private Date start;
    private Date end;     
    
    
    public Date getStart() {
        return start;
    }
 
    public void setStart(Date start) {
        this.start = start;
    }
    
    public Date getEnd() {
        return end;
    }
 
    public void setEnd(Date end) {
        this.end = end;
    }
    
    public void onDateSelect(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Date Selected"+format.format(event.getObject()));
    }
    
    public void click() {
        System.out.println("click");
    }
 
}
