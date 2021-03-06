/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.backingbeans;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookFormatsPK;
import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.BookIdentifiersPK;
import com.g4w16.entities.Books;
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
import com.g4w16.entities.Format;
import com.g4w16.entities.Genre;
import com.g4w16.entities.IdentifierType;
import com.g4w16.persistence.BookFormatsJpaController;
import com.g4w16.persistence.BookIdentifiersJpaController;
import com.g4w16.persistence.BooksJpaController;
import com.g4w16.persistence.ContributionTypeJpaController;
import com.g4w16.persistence.ContributorJpaController;
import com.g4w16.persistence.FormatJpaController;
import com.g4w16.persistence.GenreJpaController;
import com.g4w16.persistence.IdentifierTypeJpaController;
import com.g4w16.persistence.ReviewsJpaController;
import com.g4w16.persistence.SalesDetailsJpaController;
import com.g4w16.persistence.SalesJpaController;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Dan Wang, Brandon Balala
 */
@Named("booksBB")
@SessionScoped
public class AdminBooksBackingBean implements Serializable {

    private List<Books> books;
    private List<Books> saleBooks;
    private List<Books> filteredBooks;
    private Books selectedBook;
    private List<BookFormats> allBookFormats;

    private List<String> status;
    private List<Format> allFormats;
    private List<ContributionType> allContributionType;
    private List<Genre> allGenre;
    private List<Contributor> allAuthors;
    private List<IdentifierType> allIdentifierTypes;
    private List<BookIdentifiers> allBookIdentifiers;

    private Books newBook;

    private List<String> newGenres;

    private List<BookFormats> newBookFormatList;
    private String newFormat;
    private String newFile;

    private List<BookIdentifiers> newBookIdentifierList;
    private String newIdentifierType;
    private String newCode;

    private List<Contributor> newContributorList;
    private String newContributionType;
    private String newContributorName;
    
    private String isbn;

    @Inject
    private BooksJpaController booksJpaController;

    @Inject
    private GenreJpaController genreJpaController;

    @Inject
    private FormatJpaController formatJpaController;

    @Inject
    private ContributionTypeJpaController contributionTypeJpaController;

    @Inject
    private ContributorJpaController contributorJpaController;

    @Inject
    private IdentifierTypeJpaController identifierTypeJpaController;

    @Inject
    private BookIdentifiersJpaController bookIdentifiersJpaController;

    @Inject
    private BookFormatsJpaController bookFormatsJpaController;

    @Inject
    private SalesJpaController salesController;
    

    /**
     * For Inventory page
     */
    @PostConstruct
    public void init() {
        books = booksJpaController.findAllBooks();
        BigDecimal min = new BigDecimal(0);
        BigDecimal max = new BigDecimal(999999999);
        saleBooks = booksJpaController.findBookByPriceRange(min, max);
        allFormats = formatJpaController.findAllFormats();
        allContributionType = contributionTypeJpaController.findAllContributionTypes();
        allGenre = genreJpaController.findAllGenres();
        allAuthors = contributorJpaController.findAllContributors();
        allIdentifierTypes = identifierTypeJpaController.findAllIdentifierTypes();
        allBookFormats = bookFormatsJpaController.findAllBookFormats();
        allBookIdentifiers = bookIdentifiersJpaController.findAllBookIdentifiers();

        initializeNew();
    }

    /**
     * get all book identifiers
     * @return List<BookIdentifiers>
     */
    public List<BookIdentifiers> getAllBookIdentifiers() {
        return allBookIdentifiers;
    }

    /**
     * Set all book identifiers
     * @param allBookIdentifiers 
     */
    public void setAllBookIdentifiers(List<BookIdentifiers> allBookIdentifiers) {
        this.allBookIdentifiers = allBookIdentifiers;
    }

    /**
     * get isbn
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * set isbn
     * @param isbn 
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * get all book formats
     * @return allBookFormats
     */
    public List<BookFormats> getAllBookFormats() {
        return allBookFormats;
    }

    /**
     * set all book formats
     * @param allBookFormats 
     */
    public void setAllBookFormats(List<BookFormats> allBookFormats) {
        this.allBookFormats = allBookFormats;
    }

    /**
     * Get all books
     * @return books
     */
    public List<Books> getBooks() {
        return books;
    }

    /**
     * Get sale books
     * @return saleBooks
     */
    public List<Books> getSaleBooks() {
        return saleBooks;
    }

    /**
     * Get total sales
     * @param bookId
     * @return total sales
     */
    public BigDecimal getTotalSales(Integer bookId) {
        return salesController.getTotalSalesForBook(bookId);
    }

    /**
     * Get sales count
     * @return sales count
     */
    public int getSalesCount() {
        return saleBooks.size();
    }

    /**
     * Get books count
     * @return books count
     */
    public int getBooksCount() {
        return booksJpaController.getBooksCount();
    }

    /**
     * Get list of status
     * @return status
     */
    public List<String> getStatus() {
        status = new ArrayList<>(Arrays.asList("TRUE", "FALSE"));
        return status;
    }

    /**
     * Get filtered books
     * @return filteredBooks
     */
    public List<Books> getFilteredBooks() {
        return filteredBooks;
    }

    /**
     * set filtered clients
     * @param filteredBooks 
     */
    public void setFilteredClients(List<Books> filteredBooks) {
        this.filteredBooks = filteredBooks;
    }

    /**
     * get selected book
     * @return selecctedBook
     */
    public Books getSelectedBook() {
        return selectedBook;
    }

    /**
     * set selected book
     * @param selectedBook 
     */
    public void setSelectedBook(Books selectedBook) {
        this.selectedBook = selectedBook;
    }

    /**
     * Display book details, modification
     * @param book
     * @return String
     */
    public String showDetail(Books book) {
        selectedBook = booksJpaController.findBookByID(book.getId());

        initializeNew();

        for (Genre genres : selectedBook.getGenreList()) {
            newGenres.add(genres.getType());
        }

        newBookFormatList = selectedBook.getBookFormatsList();

        newBookIdentifierList = selectedBook.getBookIdentifiersList();

        newContributorList = selectedBook.getContributorList();

        return "admin_edit_book?faces-redirect=true";
    }

    /**
     * Update books in the database
     * @return String
     * @throws Exception 
     */
    public String updateBook() throws Exception {
        Books originalBook = booksJpaController.findBookByID(selectedBook.getId());

        List<Contributor> oldContributorList = originalBook.getContributorList();
        List<BookFormats> oldFormatList = originalBook.getBookFormatsList();
        List<BookIdentifiers> oldIdentifierList = originalBook.getBookIdentifiersList();

        List<Contributor> newContribList = new ArrayList<Contributor>();
        List<BookFormats> newFormatList = new ArrayList<BookFormats>();
        List<BookIdentifiers> newIdentifierList = new ArrayList<BookIdentifiers>();
        List<Genre> newGenreList = new ArrayList<Genre>();
        
        for (String genre : newGenres) {
            newGenreList.add(new Genre(genre));
        }

        for (int cntr = 0; cntr < newBookFormatList.size(); cntr++) {
            BookFormats theBookFormat = newBookFormatList.get(cntr);

            BookFormatsPK pk = theBookFormat.getBookFormatsPK();
            pk.setBook(selectedBook.getId());
            theBookFormat.setBookFormatsPK(pk);
            theBookFormat.setBooks(selectedBook);

            newFormatList.add(theBookFormat);
        }

        for (int cntr = 0; cntr < newBookIdentifierList.size(); cntr++) {
            BookIdentifiers theBookIdentifier = newBookIdentifierList.get(cntr);

            BookIdentifiersPK pk = theBookIdentifier.getBookIdentifiersPK();
            pk.setBook(selectedBook.getId());
            theBookIdentifier.setBookIdentifiersPK(pk);
            theBookIdentifier.setBooks(selectedBook);

            newIdentifierList.add(theBookIdentifier);
        }

        for (int cntr = 0; cntr < newContributorList.size(); cntr++) {
            Contributor contributor = newContributorList.get(cntr);

            contributor.setBooksList(Arrays.asList(selectedBook));

            newContribList.add(contributor);
        }

        //Delete old contributors
        for (Contributor contributor : oldContributorList) {
            if (!newContribList.contains(contributor)) {
                contributorJpaController.destroy(contributor.getId());
            }
        }
        System.out.println("Deleted old contributors");

        //Create new contributors
        for (Contributor contributor : newContribList) {
            if (!oldContributorList.contains(contributor)) {
                contributorJpaController.create(contributor);
            }
        }
        System.out.println("Created new contributors");

        //Delete old bookformats
        for (BookFormats bookFormat : oldFormatList) {
            if (!newFormatList.contains(bookFormat)) {
                bookFormatsJpaController.destroy(bookFormat.getBookFormatsPK());
            }
        }
        System.out.println("Deleted old format");

        //Create new bookformats
        for (BookFormats bookFormat : newFormatList) {
            if (!oldFormatList.contains(bookFormat)) {
                bookFormatsJpaController.create(bookFormat);
            }
        }
        System.out.println("Created new formats");

        //Delete old bookidentifiers
        for (BookIdentifiers bookIdentifiers : oldIdentifierList) {
            if (!newIdentifierList.contains(bookIdentifiers)) {
                bookIdentifiersJpaController.destroy(bookIdentifiers.getBookIdentifiersPK());
            }
        }
        System.out.println("Deleted old identifiers");

        //Create new bookidentifiers
        for (BookIdentifiers bookIdentifiers : newIdentifierList) {
            if (!oldIdentifierList.contains(bookIdentifiers)) {
                bookIdentifiersJpaController.create(bookIdentifiers);
            }
        }
        System.out.println("Created new identifiers");

        selectedBook.setGenreList(newGenreList);
        selectedBook.setContributorList(newContribList);
        selectedBook.setBookFormatsList(newFormatList);
        selectedBook.setBookIdentifiersList(newIdentifierList);

        booksJpaController.edit(selectedBook);

        updateBooks();
        initializeNew();

        return "admin_books?faces-redirect=true";
    }

    /**
     * Initialize everything and return back to book page
     * @return 
     */
    public String cancel() {
        initializeNew();
        return "admin_books?faces-redirect=true";
    }
    
    /**
     * get all contribution types
     * @return allContributionType
     */
    public List<ContributionType> getAllContributionType() {
        return allContributionType;
    }

    /**
     * get all formats
     * @return allFormats
     */
    public List<Format> getAllFormats() {
        return allFormats;
    }

    /**
     * get all genre
     * @return allGenre
     */
    public List<Genre> getAllGenre() {
        return allGenre;
    }

    /**
     * get all authors
     * @return allAuthors
     */
    public List<Contributor> getAllAuthors() {
        return allAuthors;
    }

    /**
     * get all identifier types
     * @return allIdentifierTypes
     */
    public List<IdentifierType> getAllIdentifierTypes() {
        return allIdentifierTypes;
    }

    /**
     * Edit row in the table
     * @param event
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void onRowEdit(RowEditEvent event) throws NonexistentEntityException, RollbackFailureException, Exception {
        Books book = (Books) event.getObject();
        booksJpaController.edit(book);
    }

    public void onRowCancel(RowEditEvent event) {}

    /**
     * ****************Create a book******************
     */
    
    /**
     * @return the newBook
     */
    public Books getNewBook() {
        return newBook;
    }

    /**
     * @param newBook the newBook to set
     */
    public void setNewBook(Books newBook) {
        this.newBook = newBook;
    }

    /**
     * @return the newGenre
     */
    public List<String> getNewGenres() {
        return newGenres;
    }

    /**
     * @param newGenres the newGenres to set
     */
    public void setNewGenres(List<String> newGenres) {
        this.newGenres = newGenres;
    }

    /**
     * @return the newBookFormatList
     */
    public List<BookFormats> getNewBookFormatList() {
        return newBookFormatList;
    }

    /**
     * @param newBookFormatList the newBookFormatList to set
     */
    public void setNewBookFormatList(List<BookFormats> newBookFormatList) {
        this.newBookFormatList = newBookFormatList;
    }

    /**
     * @return the newFormat
     */
    public String getNewFormat() {
        return newFormat;
    }

    /**
     * @param newFormat the newFormat to set
     */
    public void setNewFormat(String newFormat) {
        this.newFormat = newFormat;
    }

    /**
     * @return the newFile
     */
    public String getNewFile() {
        return newFile;
    }

    /**
     * @param newFile the newFile to set
     */
    public void setNewFile(String newFile) {
        this.newFile = newFile;
    }

    /**
     * @return the newBookIdentifierList
     */
    public List<BookIdentifiers> getNewBookIdentifierList() {
        return newBookIdentifierList;
    }

    /**
     * @param newBookIdentifierList the newBookIdentifierList to set
     */
    public void setNewBookIdentifierList(List<BookIdentifiers> newBookIdentifierList) {
        this.newBookIdentifierList = newBookIdentifierList;
    }

    /**
     * @return the newIdentifierType
     */
    public String getNewIdentifierType() {
        return newIdentifierType;
    }

    /**
     * @param newIdentifierType the newIdentifierType to set
     */
    public void setNewIdentifierType(String newIdentifierType) {
        this.newIdentifierType = newIdentifierType;
    }

    /**
     * @return the newCode
     */
    public String getNewCode() {
        return newCode;
    }

    /**
     * @param newCode the newCode to set
     */
    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    /**
     * @return the newContributorList
     */
    public List<Contributor> getNewContributorList() {
        return newContributorList;
    }

    /**
     * @param newContributorList the newContributorList to set
     */
    public void setNewContributorList(List<Contributor> newContributorList) {
        this.newContributorList = newContributorList;
    }

    /**
     * @return the newContributionType
     */
    public String getNewContributionType() {
        return newContributionType;
    }

    /**
     * @param newContributionType the newContributionType to set
     */
    public void setNewContributionType(String newContributionType) {
        this.newContributionType = newContributionType;
    }

    /**
     * @return the newContributorName
     */
    public String getNewContributorName() {
        return newContributorName;
    }

    /**
     * @param newContributorName the newContributionName to set
     */
    public void setNewContributorName(String newContributorName) {
        this.newContributorName = newContributorName;
    }

    /**
     * Show create book page
     * @return String
     */
    public String showCreatePage() {
        initializeNew();
        return "admin_create_book?faces-redirect=true";
    }

    /**
     * Create book in the database
     * @return String
     * @throws Exception 
     */
    public String createBook() throws Exception {
        List<Genre> genreList = new ArrayList<Genre>();
        for (String genre : newGenres) {
            genreList.add(new Genre(genre));
        }

        newBook.setGenreList(genreList);
        booksJpaController.create(newBook);
        System.out.println("Book created!");

        for (int cntr = 0; cntr < newBookFormatList.size(); cntr++) {
            BookFormats theBookFormat = newBookFormatList.get(cntr);

            BookFormatsPK pk = theBookFormat.getBookFormatsPK();
            pk.setBook(newBook.getId());
            theBookFormat.setBookFormatsPK(pk);
            theBookFormat.setBooks(newBook);

            bookFormatsJpaController.create(theBookFormat);
        }
        System.out.println("Formats created!");

        for (int cntr = 0; cntr < newBookIdentifierList.size(); cntr++) {
            BookIdentifiers theBookIdentifier = newBookIdentifierList.get(cntr);

            BookIdentifiersPK pk = theBookIdentifier.getBookIdentifiersPK();
            pk.setBook(newBook.getId());
            theBookIdentifier.setBookIdentifiersPK(pk);
            theBookIdentifier.setBooks(newBook);

            bookIdentifiersJpaController.create(theBookIdentifier);
        }
        System.out.println("Identifiers created!");

        for (int cntr = 0; cntr < newContributorList.size(); cntr++) {
            Contributor contributor = newContributorList.get(cntr);

            contributor.setBooksList(Arrays.asList(newBook));

            contributorJpaController.create(contributor);
        }
        System.out.println("Identifiers created!");

        updateBooks();
        initializeNew();

        return "admin_books?faces-redirect=true";
    }

    /**
     * Create new book format
     */
    public void createBookFormat() {
        System.out.println("In create book format");

        int replace = -1;
        for (int cntr = 0; cntr < newBookFormatList.size(); cntr++) {
            String formatStr = newBookFormatList.get(cntr).getFormat1().getType();
            if (formatStr.equals(newFormat)) {
                replace = cntr;
                break;
            }
        }

        BookFormats bookFormat = new BookFormats();
        BookFormatsPK pk = new BookFormatsPK(0, newFormat);
        bookFormat.setBookFormatsPK(pk);
        Format format = new Format(newFormat);
        bookFormat.setFormat1(format);
        bookFormat.setFile(newFile);

        if (replace == -1) {
            newBookFormatList.add(bookFormat);
            System.out.println("ADDED TO LIST");
        } else {
            newBookFormatList.set(replace, bookFormat);
            System.out.println("REPLACED IN LIST");
        }

        System.out.println("Added to format list");

        clearFormatFields();
    }

    /**
     * Create new book identifier
     */
    public void createBookIdentifier() {
        System.out.println("In create book format");

        int replace = -1;
        for (int cntr = 0; cntr < newBookIdentifierList.size(); cntr++) {
            String identifierType = newBookIdentifierList.get(cntr).getIdentifierType().getType();
            if (identifierType.equals(newIdentifierType)) {
                replace = cntr;
                break;
            }
        }

        BookIdentifiers bookIdentifiers = new BookIdentifiers();
        BookIdentifiersPK pk = new BookIdentifiersPK(0, newIdentifierType);
        bookIdentifiers.setBookIdentifiersPK(pk);
        IdentifierType type = new IdentifierType(newIdentifierType);
        bookIdentifiers.setIdentifierType(type);
        bookIdentifiers.setCode(newCode);

        if (replace == -1) {
            newBookIdentifierList.add(bookIdentifiers);
            System.out.println("ADDED TO LIST");
        } else {
            newBookIdentifierList.set(replace, bookIdentifiers);
            System.out.println("REPLACED IN LIST");
        }

        System.out.println("Added to identifier list");

        clearIdentifierFields();
    }

    /**
     * Create new contributor
     */
    public void createContributor() {
        System.out.println("In create contributor");

        int replace = -1;
        for (int cntr = 0; cntr < newContributorList.size(); cntr++) {
            Contributor contrib = newContributorList.get(cntr);
            String name = contrib.getName();
            String type = contrib.getContribution().getType();
            if (name.equals(newContributorName) && type.equals(newContributionType)) {
                replace = cntr;
                break;
            }
        }

        Contributor contributor = new Contributor();
        ContributionType type = new ContributionType(newContributionType);
        contributor.setContribution(type);
        contributor.setName(newContributorName);

        if (replace == -1) {
            newContributorList.add(contributor);
            System.out.println("ADDED TO LIST");
        } else {
            newContributorList.set(replace, contributor);
            System.out.println("REPLACED IN LIST");
        }

        System.out.println("Added to identifier list");

        clearContributorFields();
    }

    /**
     * Initialize all variables
     */
    public void initializeNew() {
        newBook = new Books();

        newGenres = new ArrayList<String>();
        newBookFormatList = new ArrayList<BookFormats>();
        newBookIdentifierList = new ArrayList<BookIdentifiers>();
        newContributorList = new ArrayList<Contributor>();

        clearFormatFields();
        clearIdentifierFields();
        clearContributorFields();
    }

    /**
     * Update the book list
     */
    private void updateBooks() {
        books = booksJpaController.findAllBooks();
    }

    /**
     * Clear format fields
     */
    private void clearFormatFields() {
        newFormat = "";
        newFile = "";
    }

    /**
     * Clear identifier fields
     */
    private void clearIdentifierFields() {
        newIdentifierType = "";
        newCode = "";
    }

    /**
     * Clear contributor fields
     */
    private void clearContributorFields() {
        newContributionType = "";
        newContributorName = "";
    }

    /**
     * Remove contributor from list
     * @param theContributor 
     */
    public void removeContributor(Contributor theContributor) {
        newContributorList.remove(theContributor);
    }

    /**
     * Remove book identifiers from list
     * @param bookIdentifiers 
     */
    public void removeBookIdentifier(BookIdentifiers bookIdentifiers) {
        newBookIdentifierList.remove(bookIdentifiers);
    }

    /**
     * Remove book format from list
     * @param bookFormat 
     */
    public void removeBookFormat(BookFormats bookFormat) {
        newBookFormatList.remove(bookFormat);
    }

    /**
     * Remove genre from list
     * @param genre 
     */
    public void removeGenre(String genre) {
        newGenres.remove(genre);
    }
}
