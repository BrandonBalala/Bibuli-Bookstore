package com.g4w16.jsf.controller;

import com.g4w16.entities.Reviews;
import com.g4w16.jsf.controller.util.JsfUtil;
import com.g4w16.jsf.controller.util.PaginationHelper;
import com.g4w16.jsf.bean.ReviewsFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("reviewsController")
@SessionScoped
public class ReviewsController implements Serializable {

    private Reviews current;
    private DataModel items = null;
    @EJB
    private com.g4w16.jsf.bean.ReviewsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ReviewsController() {
    }

    public Reviews getSelected() {
        if (current == null) {
            current = new Reviews();
            current.setReviewsPK(new com.g4w16.entities.ReviewsPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ReviewsFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Reviews) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Reviews();
        current.setReviewsPK(new com.g4w16.entities.ReviewsPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getReviewsPK().setBook(current.getBooks().getId());
            current.getReviewsPK().setClient(current.getClient1().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Reviews) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getReviewsPK().setBook(current.getBooks().getId());
            current.getReviewsPK().setClient(current.getClient1().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Reviews) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Reviews getReviews(com.g4w16.entities.ReviewsPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Reviews.class)
    public static class ReviewsControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReviewsController controller = (ReviewsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reviewsController");
            return controller.getReviews(getKey(value));
        }

        com.g4w16.entities.ReviewsPK getKey(String value) {
            com.g4w16.entities.ReviewsPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.g4w16.entities.ReviewsPK();
            key.setBook(Integer.parseInt(values[0]));
            key.setClient(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(com.g4w16.entities.ReviewsPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getBook());
            sb.append(SEPARATOR);
            sb.append(value.getClient());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Reviews) {
                Reviews o = (Reviews) object;
                return getStringKey(o.getReviewsPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Reviews.class.getName());
            }
        }

    }

}
