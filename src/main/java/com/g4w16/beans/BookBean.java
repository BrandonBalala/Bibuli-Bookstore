package com.g4w16.beans;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.Books;
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
import com.g4w16.entities.Genre;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("book")
@RequestScoped
public class BookBean {

    private int id;
    private ArrayList<BookIdentifierBean> identifiers;
    private String title;
    private ArrayList<ContributorBean> contributors;
    private String publisher;
    private int numberOfPages;
    private ArrayList<String> genres;
    private String description;
    private ArrayList<BookFormatBean> formats;
    private BigDecimal wholesalePrice;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private LocalDateTime publicationDate;
    private LocalDateTime dateEntered;
    private boolean removalStatus;  //true if book not available, false if it is

    /**
     * Default constructor for creating an empty book bean and setting its fields
     */
    public BookBean() {
        this(0, new ArrayList<BookIdentifierBean>(), "", new ArrayList<ContributorBean>(), "", 0, new ArrayList<String>(), "", new ArrayList<BookFormatBean>(), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), LocalDateTime.now(),
                LocalDateTime.now(), "", false);
    }

    /**
     * Constructor for creating a book bean
     *
     * @param id
     * @param title
     * @param contributors
     * @param publisher
     * @param numberOfPages
     * @param genres
     * @param description
     * @param formats
     * @param wholesalePrice
     * @param listPrice
     * @param salePrice
     * @param publicationDate
     * @param dateEntered
     * @param bookCoverFilename
     * @param removalStatus
     */
    public BookBean(final int id, final ArrayList<BookIdentifierBean> identifiers, final String title, final ArrayList<ContributorBean> contributors, final String publisher, final int numberOfPages,
            final ArrayList<String> genres, final String description, final ArrayList<BookFormatBean> formats, final BigDecimal wholesalePrice, final BigDecimal listPrice,
            final BigDecimal salePrice, final LocalDateTime publicationDate, final LocalDateTime dateEntered, final String bookCoverFilename,
            final boolean removalStatus) {
        super();
        this.id = id;
        this.identifiers = identifiers;
        this.title = title;
        this.contributors = contributors;
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.genres = genres;
        this.description = description;
        this.formats = formats;
        this.wholesalePrice = wholesalePrice;
        this.listPrice = listPrice;
        this.salePrice = salePrice;
        this.publicationDate = publicationDate;
        this.dateEntered = dateEntered;
        this.removalStatus = removalStatus;
    }
    
//    public BookBean(Books book){
//        this.identifiers = new ArrayList<BookIdentifierBean>();
//        this.contributors = new ArrayList<ContributorBean>();
//        this.genres = new ArrayList<String>();
//        this.formats = new ArrayList<BookFormatBean>();
//        
//        this.id = book.getId();
//        this.title = book.getTitle();
//        this.description = book.getDescription();
//        this.publisher = book.getPublisher();
//        this.numberOfPages = book.getPages();
//        this.wholesalePrice = book.getWholesalePrice();
//        this.listPrice = book.getListPrice();
//        this.salePrice = book.getSalePrice();
//        this.publicationDate = LocalDateTime.ofInstant(book.getPubDate().toInstant(), ZoneId.systemDefault());
//        this.dateEntered = LocalDateTime.ofInstant(book.getDateEntered().toInstant(), ZoneId.systemDefault());
//        this.removalStatus = book.getRemovalStatus();
//        
//        for(BookIdentifiers bookIdentifier : book.getBookIdentifiersList()){
//            BookIdentifierBean bookIdent = new BookIdentifierBean();
//            bookIdent.setCode(bookIdentifier.getCode());
//            bookIdent.setType(bookIdentifier.getBookIdentifiersPK().getType());
//            identifiers.add(bookIdent);
//        }
//        
//        for(Contributor contributor : book.getContributorList()){
//            ContributorBean contrib = new ContributorBean();
//            contrib.setId(contributor.getId());
//            contrib.setContribution(contrib.getContribution());
//            contrib.setName(contributor.getName());
//            contributors.add(contrib);
//        }
//        
//        for(Genre genre: book.getGenreList()){
//            genres.add(genre.getType());
//        }
//        
//        for(BookFormats bookFormat : book.getBookFormatsList()){
//            BookFormatBean bookFrmt = new BookFormatBean();
//            bookFrmt.setFile(bookFormat.getFile());
//            bookFrmt.setFormat(bookFormat.getBookFormatsPK().getFormat());
//            formats.add(bookFrmt);
//        }
//    }

    /**
     * Gets the book id
     * 
     * @return the id
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the book id
     * 
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the identifiers
     * 
     * @return the identifiers
     */
    public final ArrayList<BookIdentifierBean> getIdentifiers() {
        return identifiers;
    }

    /**
     * Gets the title
     * 
     * @return the title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * 
     * @param title the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the contributors
     * 
     * @return the contributors
     */
    public final ArrayList<ContributorBean> getContributors() {
        return contributors;
    }

    /**
     * Gets the publisher
     * 
     * @return the publisher
     */
    public final String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher
     * 
     * @param publisher the publisher to set
     */
    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the number of pages
     * 
     * @return the numberOfPages
     */
    public final int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the number of pages
     * 
     * @param numberOfPages the numberOfPages to set
     */
    public void setNumberOfPages(final int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * Gets the genres
     * 
     * @return the genres
     */
    public final ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * Gets the description
     * 
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description
     * 
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the formats
     * 
     * @return the formats
     */
    public final ArrayList<BookFormatBean> getFormats() {
        return formats;
    }

    /**
     * Gets the wholesale price
     * 
     * @return the wholesalePrice
     */
    public final BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    /**
     * Sets the wholesale price
     * 
     * @param wholesalePrice the wholesalePrice to set
     */
    public void setWholesalePrice(final BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    /**
     * Gets the list price
     * 
     * @return the listPrice
     */
    public final BigDecimal getListPrice() {
        return listPrice;
    }

    /**
     * Sets the list price
     * 
     * @param listPrice the listPrice to set
     */
    public void setListPrice(final BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    /**
     * Gets the sale price
     * 
     * @return the salePrice
     */
    public final BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the sale price
     * 
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(final BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Gets the publication date
     * 
     * @return the publicationDate
     */
    public final LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the publication date
     * 
     * @param publicationDate the publicationDate to set
     */
    public void setPublicationDate(final LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Gets date entered into database
     * 
     * @return the dateEntered
     */
    public final LocalDateTime getDateEntered() {
        return dateEntered;
    }

    /**
     * Sets date entered into database
     * 
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(final LocalDateTime dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * Gets removal status
     * 
     * @return the removalStatus
     */
    public final boolean getRemovalStatus() {
        return removalStatus;
    }

    /**
     * Sets the removal status
     * 
     * @param removalStatus the removalStatus to set
     */
    public void setRemovalStatus(final boolean removalStatus) {
        this.removalStatus = removalStatus;
    }

}