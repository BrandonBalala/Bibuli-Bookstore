package com.g4w16.jsf.controller;

import com.g4w16.entities.BookFormats;
import com.g4w16.jsf.controller.util.JsfUtil;
import com.g4w16.jsf.controller.util.PaginationHelper;
import com.g4w16.jsf.bean.BookFormatsFacade;

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

@Named("bookFormatsController")
@SessionScoped
public class BookFormatsController implements Serializable {

    private BookFormats current;
    private DataModel items = null;
    @EJB
    private com.g4w16.jsf.bean.BookFormatsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public BookFormatsController() {
    }

    public BookFormats getSelected() {
        if (current == null) {
            current = new BookFormats();
            current.setBookFormatsPK(new com.g4w16.entities.BookFormatsPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private BookFormatsFacade getFacade() {
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
        current = (BookFormats) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new BookFormats();
        current.setBookFormatsPK(new com.g4w16.entities.BookFormatsPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getBookFormatsPK().setFormat(current.getFormat1().getType());
            current.getBookFormatsPK().setBook(current.getBooks().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookFormatsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (BookFormats) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getBookFormatsPK().setFormat(current.getFormat1().getType());
            current.getBookFormatsPK().setBook(current.getBooks().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookFormatsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (BookFormats) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookFormatsDeleted"));
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

    public BookFormats getBookFormats(com.g4w16.entities.BookFormatsPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BookFormats.class)
    public static class BookFormatsControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BookFormatsController controller = (BookFormatsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bookFormatsController");
            return controller.getBookFormats(getKey(value));
        }

        com.g4w16.entities.BookFormatsPK getKey(String value) {
            com.g4w16.entities.BookFormatsPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.g4w16.entities.BookFormatsPK();
            key.setBook(Integer.parseInt(values[0]));
            key.setFormat(values[1]);
            return key;
        }

        String getStringKey(com.g4w16.entities.BookFormatsPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getBook());
            sb.append(SEPARATOR);
            sb.append(value.getFormat());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof BookFormats) {
                BookFormats o = (BookFormats) object;
                return getStringKey(o.getBookFormatsPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BookFormats.class.getName());
            }
        }

    }

}
