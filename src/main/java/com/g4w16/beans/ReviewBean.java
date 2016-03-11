package com.g4w16.beans;

import java.time.LocalDateTime;

public class ReviewBean {

    private int bookId;
    private LocalDateTime creationDate;
    private int clientId;
    private int rating; //1 to 5
    private String reviewText;
    private Boolean approvalStatus; //true if approved, false if it isn't 
    
    /**
     * Default constructor for creating an empty review bean
     */
    public ReviewBean() {
        this(0,LocalDateTime.now(), 0, 0, "", false);
    }

    /**
     * Constructor for creating a review bean and setting its fields
     * 
     * @param bookId
     * @param creationDate
     * @param clientId
     * @param rating
     * @param reviewText
     * @param approvalStatus 
     */
    public ReviewBean(final int bookId, final LocalDateTime creationDate, final int clientId, final int rating, final String reviewText,
            final Boolean approvalStatus) {
        super();
        this.bookId = bookId;
        this.creationDate = creationDate;
        this.clientId = clientId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.approvalStatus = approvalStatus;
    }

    /**
     * Gets the id
     * 
     * @return the id
     */
    public final int getBookId() {
        return bookId;
    }

    /**
     * Sets the id
     * 
     * @param id the id to set
     */
    public final void setBookId(final int bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the creation date
     * 
     * @return the creationDate
     */
    public final LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date
     * 
     * @param creationDate the creationDate to set
     */
    public final void setCreationDate(final LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the client id
     * 
     * @return the clientId
     */
    public final int getClientId() {
        return clientId;
    }

    /**
     * Sets the client id
     * 
     * @param clientId the clientId to set
     */
    public final void setClientId(final int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the rating
     * 
     * @return the rating
     */
    public final int getRating() {
        return rating;
    }

    /**
     * Sets the rating
     * 
     * @param rating the rating to set
     */
    public final void setRating(final int rating) {
        this.rating = rating;
    }

    /**
     * Gets the review text
     * 
     * @return the reviewText
     */
    public final String getReviewText() {
        return reviewText;
    }

    /**
     * Sets the review text
     * 
     * @param reviewText the reviewText to set
     */
    public final void setReviewText(final String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Gets the approval status
     * 
     * @return the approvalStatus
     */
    public final Boolean getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Sets the approval status
     * 
     * @param approvalStatus the approvalStatus to set
     */
    public final void setApprovalStatus(final Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

}
