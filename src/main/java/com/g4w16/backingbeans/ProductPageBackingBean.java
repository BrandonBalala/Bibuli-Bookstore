package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Genre;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.ReviewsPK;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.ClientJpaController;
import com.g4w16.persistence.ReviewsJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Brandon Balala
 */
@Named("productPageBB")
@SessionScoped
public class ProductPageBackingBean implements Serializable {

    private Books book;
    private Reviews review;
    private List<Books> recommendedBookList;
    
    private static final int NUM_BOOKS = 4;

    @Inject
    ReviewsJpaController reviewController;

    @Inject
    BooksJpaController bookController;

    @Inject
    ClientJpaController clientController;

    public Reviews getReview() {
        if (review == null) {
            review = new Reviews();
        }

        return review;
    }

    public void setReview(Reviews review) {
        this.review = review;
    }

    public Books getBook() {
        if (book == null) {
            book = new Books();
        }

        return book;
    }

    public void setBook(Books book) {
        this.book = book;

        //Set recommended books
        setRecommendedBookList(NUM_BOOKS);
        
        //Clear previous review if existed
        review = new Reviews();
    }

    public void createReview() {
        ReviewsPK pk = new ReviewsPK();
        pk.setBook(book.getId());
        pk.setClient(69); //CHANGE LATER

        if (!reviewController.reviewExists(pk)) {
            review.setCreationDate(Date.from(Instant.now()));
            review.setApproval(false);
            review.setClient1(clientController.findClientById(2));
            review.setReviewsPK(pk);

            try {
                reviewController.create(review);
            } catch (Exception ex) {
                Logger.getLogger(ProductPageBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Logger.getLogger(ProductPageBackingBean.class.getName()).log(Level.SEVERE, "You already wrote a review for this book");
        }
    }

    public String getFormattedBookPubDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(book.getPubDate());
    }

    public String getFormattedReviewCreationDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(review.getCreationDate());
    }

    public int getAverageRating() {
        if (book.getReviewsList().isEmpty()) {
            return 0;
        }

        int numBooks = book.getReviewsList().size();
        int sum = 0;
        for (Reviews rev : book.getReviewsList()) {
            sum += rev.getRating();
        }

        return sum / numBooks;
    }

    public int getSavingsPercentage() {
        BigDecimal sale = book.getSalePrice();
        BigDecimal retail = book.getListPrice();

        double percentageInDecimal = (sale.doubleValue()) / (retail.doubleValue());
        double savingsInDecimal = 1 - percentageInDecimal;
        int savingsPercentage = (int) (savingsInDecimal * 100);

        return savingsPercentage;
    }

    public void addBookToCart() {

    }
    
    public List<Books> getRecommendedBookList(){
        return recommendedBookList;
    }

    private void setRecommendedBookList(int numBooks) {
        List<Genre> genreList = book.getGenreList();
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(genreList.size());
        //If book has multipe genres pick one genre at random to display in recommended books tab
        String genre = genreList.get(index).getType();

        List<Books> recommendedBooks = bookController.findRecommendedBook(genre, book.getId());
        Collections.shuffle(recommendedBooks);

        //To avoid index out of bounds exception
        if (numBooks > recommendedBooks.size()) {
            numBooks = recommendedBooks.size();
        }

        this.recommendedBookList = recommendedBooks.subList(0, numBooks);
    }

    public void displayProductPageRecommended(Books book) throws IOException{
        setBook(book);

//        return "product-page";
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
