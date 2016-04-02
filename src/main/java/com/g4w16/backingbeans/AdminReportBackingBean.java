/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;


import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
 

/**
 *
 * @author wangdan
 */

@ManagedBean(name="reportBB")
@SessionScoped
public class AdminReportBackingBean implements Serializable { 
    
    private Date start;
    private Date end;     
    
    @PostConstruct
    public void init(){
        start=new Date();
        end=new Date();
    }
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
    
   
    
    public void click() {
        System.out.println("start:"+start);
        System.out.println("end:"+end);
    }
 
}
