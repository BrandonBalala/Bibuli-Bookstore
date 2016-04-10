package com.g4w16.backingbeans;

import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Brandon Balala
 */
@Named("masterLayoutBB")
@SessionScoped
public class MasterLayoutBackingBean implements Serializable {

    private List<Genre> genreList;
    private List<Format> formatList;

    @Inject
    private GenreJpaController genreController;

    @Inject
    private FormatJpaController formatController;

    @Inject
    private BooksJpaController bookController;

    @Inject
    private ResultBackingBean resultBB;

    /**
     * Get list of all genres
     * @return genreList
     */
    public List<Genre> getGenreList() {
        if (genreList == null) {
            genreList = new ArrayList<Genre>();
        }

        return genreList;
    }

    /**
     * Set list of genre
     * @param genreList 
     */
    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    /**
     * Get all format list
     * @return formatList
     */
    public List<Format> getFormatList() {
        if (formatList == null) {
            formatList = new ArrayList<Format>();
        }

        return formatList;
    }

    /**
     * Set format list
     * @param formatList 
     */
    public void setFormatList(List<Format> formatList) {
        this.formatList = formatList;
    }

    @PostConstruct
    public void init() {
        //genreList = genreController.findAllGenres();
        genreList = genreController.findAllUsedGenres();
        formatList = formatController.findAllFormats();
    }

    /**
     * Redirect to results page, display all books
     * @return String
     */
    public String displayAllBooks() {
        resultBB.setBookList(bookController.findAllBooks());

        return "results?faces-redirect=true";
    }

    /**
     * Display results page for all books by genre
     * @param genre
     * @return String
     */
    public String displayBooksByGenre(String genre) {
        resultBB.setBookList(bookController.findBooksByGenre(genre));
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        String cookie = genre;
        response.addCookie(new Cookie("lastGenre", cookie));

        return "results?faces-redirect=true";
    }

    /**
     * Display results page for all books by format
     * @param format
     * @return 
     */
    public String displayBooksByFormat(String format) {
        resultBB.setBookList(bookController.findBooksByFormat(format));

        return "results?faces-redirect=true";
    }

}
