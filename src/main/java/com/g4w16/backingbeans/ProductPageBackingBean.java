package com.g4w16.backingbeans;

import com.g4w16.entities.Books;
import com.g4w16.entities.Contributor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Brandon Balala
 */
@Named("productPageBB")
@SessionScoped
public class ProductPageBackingBean implements Serializable {

    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private Books book;
    private Reviews review;
    private boolean edit;
    private List<Books> recommendedBookList;
    private List<Books> sameContributorsBookList;

    private static final int NUM_BOOKS = 4;

    @Inject
    private ReviewsJpaController reviewController;

    @Inject
    private BooksJpaController bookController;

    @Inject
    private ClientJpaController clientController;

    @Inject
    private ShoppingCartBackingBean cartBB;

    @Inject
    private MessageManagedBean messageBean;

    public Reviews getReview() {
        if (review == null) {
            review = new Reviews();
        }

        return review;
    }

    public void setReview(Reviews review) {
        this.review = review;
    }

    public boolean getEdit() {
        return edit;
    }

    public Books getBook() {
        if (book == null) {
            book = new Books();
        }

        return book;
    }

    public void setBook(Books book) {
        this.book = book;
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        String cookie = "";
        for(Genre g :book.getGenreList())
        {
            cookie += g.getType();
        }
        response.addCookie(new Cookie("lastGenre",cookie));
        //Set recommended books
        setRecommendedBookList(NUM_BOOKS);
        setSameContributorsBookList(NUM_BOOKS);

        //Clear previous review if existed
        if (session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated")) {
            edit = true;
            review = reviewController.findReviewByUserAndBook(book.getId(), (int) session.getAttribute("client"));
        }

        if (review == null) {
            edit = false;
            review = new Reviews();
        }
    }

    public void createReview() {
        ReviewsPK pk = new ReviewsPK();
        pk.setBook(book.getId());
        pk.setClient((int) session.getAttribute("client"));

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
            try {
                review.setApproval(false);
                reviewController.edit(review);
            } catch (Exception ex) {
                Logger.getLogger(ProductPageBackingBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Logger.getLogger(ProductPageBackingBean.class.getName()).log(Level.SEVERE, "You already wrote a review for this book");
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

    public List<Books> getRecommendedBookList() {
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

    public List<Books> getSameContributorsBookList() {
        return sameContributorsBookList;
    }

    public void setSameContributorsBookList(int numBooks) {
        List<Books> booksByContributors = new ArrayList<Books>();

        for (Contributor contributor : book.getContributorList()) {
            List<Books> temp = bookController.findBooksByContributorName(contributor.getName(), book.getId());

            if (temp == null || temp.isEmpty()) {
                continue;
            }

            booksByContributors.addAll(temp);
        }

        Collections.shuffle(booksByContributors);

        //To avoid index out of bounds exception
        if (numBooks > booksByContributors.size()) {
            numBooks = booksByContributors.size();
        }

        this.sameContributorsBookList = booksByContributors.subList(0, numBooks);
    }

    public void displayProductPageRecommended(Books book) throws IOException {
        setBook(book);

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void addBookToCart() {
        List<Books> bookList = cartBB.getBookList();

        if (!bookList.contains(book)) {
            cartBB.addBookToCart(book);
            messageBean.setMessage(book.getTitle() + " has been added to your cart!");
        } else {
            messageBean.setMessage(book.getTitle() + " is already in your cart!");
        }
    }

    public void displayProductPageSameContrib(Books book) throws IOException {
        setBook(book);

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
