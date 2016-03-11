/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Books;
import com.g4w16.entities.*;
import com.g4w16.persistence.exceptions.PreexistingEntityException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author BRANDON-PC
 */
@RunWith(Arquillian.class)
//@Ignore
public class BooksJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private BooksJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public BooksJpaControllerTest() {
    }

    @Deployment
    public static WebArchive deploy() {

        // Use an alternative to the JUnit assert library called AssertJ
        // Need to reference MySQL driver as it is not part of either
        // embedded or remote
        final File[] dependencies = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("mysql:mysql-connector-java",
                        "org.assertj:assertj-core").withoutTransitivity()
                .asFile();

        // The webArchive is the special packaging of your project
        // so that only the test cases run on the server or embedded
        // container
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addPackage(BooksJpaController.class.getPackage())
                .addPackage(Books.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }

    /**
     * Test of create method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testCreateBook() throws Exception {
        System.out.println("create");
        Books book = createMockBook();
        Books result = instance.findBookByID(book.getId());

        assertEquals(book, result);
    }

    /**
     * Test of create method, of class BooksJpaController.
     */
    @Test(expected = PreexistingEntityException.class)
    @Ignore
    public void testCreateDuplicateBook() throws Exception {
        System.out.println("testCreateDuplicateBook");
        createMockBook();
        createMockBook();
    }

    /**
     * Test of create method, of class BooksJpaController.
     */
    @Test(expected = Exception.class)
    @Ignore
    public void testCreateIncompleteBook() throws Exception {
        System.out.println("testCreateIncompleteBook");
        createIncompleteMockBook();
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditChangeConttributor() throws Exception {
        System.out.println("testEditChangeConttributor");
        Books books = createMockBook();
        books.getContributorList().get(0).setName("BRANDON");

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        System.out.println("Changed name: " + books.getContributorList().get(0).getName());
        System.out.println("Retrieved name: " + result.getContributorList().get(0).getName());

        assertEquals(books.getContributorList().get(0).getName(), result.getContributorList().get(0).getName());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditAddContributor() throws Exception {
        System.out.println("testEditChangeContributor");
        Books books = createMockBook();

        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Author"));
        books.getContributorList().add(contributor);

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(books.getContributorList().get(1).getName(), result.getContributorList().get(1).getName());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditRemoveContributor() throws Exception {
        System.out.println("testEditRemoveContributor");
        Books books = createMockBook();
        int expRes = 1;

        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Author"));
        books.getContributorList().add(contributor);

        instance.edit(books);

        books.getContributorList().remove(contributor);

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(expRes, result.getContributorList().size());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditAddGenre() throws Exception {
        System.out.println("testEditAddGenre");
        Books books = createMockBook();
        books.getGenreList().add(new Genre("History"));

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        System.out.println("Genre added: " + books.getGenreList().get(2).getType());
        System.out.println("Retrieved genre: " + result.getGenreList().get(2).getType());

        assertEquals(books.getGenreList().get(2).getType(), result.getGenreList().get(2).getType());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditRemoveGenre() throws Exception {
        System.out.println("testEditRemoveGenre");
        int expRes = 1;
        Books books = createMockBook();
        books.getGenreList().remove(new Genre("Horror"));

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(expRes, result.getGenreList().size());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test(expected = Exception.class)
    @Ignore
    public void testEditRemoveAllGenres() throws Exception {
        System.out.println("testEditRemoveGenre");
        Books books = createMockBook();
        books.getGenreList().clear();

        instance.edit(books);
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEdit() throws Exception {
        System.out.println("edit");
        Books books = createMockBook();
        books.setTitle("CHANGED");

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        System.out.println("Changed title: " + books.getTitle());
        System.out.println("Retrieved title: " + result.getTitle());

        assertEquals(books.getTitle(), result.getTitle());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditAddBookFormat() throws Exception {
        System.out.println("edit");
        Books books = createMockBook();

        BookFormatsPK pk = new BookFormatsPK(0, "EPUB");
        BookFormats bookformat = new BookFormats(pk);
        bookformat.setFile("bookOfLies.epub");
        books.getBookFormatsList().add(bookformat);
        books.setListPrice(BigDecimal.valueOf(12345.00));

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(books.getBookFormatsList().get(1).getBookFormatsPK(), result.getBookFormatsList().get(1).getBookFormatsPK());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testEditBookFormat() throws Exception {
        System.out.println("edit");
        Books books = createMockBook();

        books.getBookFormatsList().get(0).setFile("CHANGED");

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(books.getBookFormatsList().get(0).getFile(), result.getBookFormatsList().get(0).getFile());
    }

    /**
     * Test of edit method, of class BooksJpaController.
     */
    @Test
    @Ignore //STILL NOT WORKING!!!
    public void testEditBookFormat2() throws Exception {
        System.out.println("edit");
        Books books = createMockBook();

        books.getBookFormatsList().get(0).getBookFormatsPK().setFormat("EPUB");

        instance.edit(books);

        Books result = instance.findBookByID(books.getId());

        assertEquals(books.getBookFormatsList().get(0).getBookFormatsPK().getFormat(), result.getBookFormatsList().get(1).getBookFormatsPK().getFormat());
    }

    /**
     * Test of destroy method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = 1;

        instance.destroy(id);

        Books book = instance.findBookByID(id);

        assertEquals(null, book);
    }

    /**
     * Test of findAllBooks method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindAllBooks() {
        System.out.println("findAllBooks");

        int expResult = 100;
        List<Books> result = instance.findAllBooks();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBooksEntities method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksEntities() {
        System.out.println("findBooksEntities");
        int maxResults = 50;
        int firstResult = 75;

        int expResult = 25;
        List<Books> result = instance.findBooksEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBookByID method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBookByID() throws Exception {
        System.out.println("findBookByID");

        Books expResult = createMockBook();

        Books result = instance.findBookByID(expResult.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of getBooksCount method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testGetBooksCount() {
        System.out.println("getBooksCount");

        int expResult = 100;
        int result = instance.getBooksCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of findBooksByTitle method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByTitle() {
        System.out.println("findBooksByTitle");
        String title = "Ghost World";

        List<Books> result = instance.findBooksByTitle(title);
        boolean working = true;
        for (Books book : result) {
            if (!(book.getTitle()).equals(title)) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBooksByPublisher method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByPublisher() {
        System.out.println("findBooksByPublisher");
        String publisher = "AMMO Books";

        List<Books> result = instance.findBooksByPublisher(publisher);
        boolean working = true;
        for (Books book : result) {
            if (!(book.getPublisher()).equals(publisher)) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBookByPriceRange method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBookByPriceRange() {
        System.out.println("findBookByPriceRange");
        BigDecimal min = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.valueOf(1000.00);

        List<Books> result = instance.findBookByPriceRange(min, max);
        System.out.println("SIZE: " + result.size());
        boolean working = true;
        for (Books book : result) {
            BigDecimal listPrice = book.getListPrice();
            if (listPrice.compareTo(max) == 1 || listPrice.compareTo(min) == -1) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBooksByYear method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByYear() {
        System.out.println("findBooksByYear");
        int year = 2014;

        List<Books> result = instance.findBooksByYear(year);
        boolean working = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        for (Books book : result) {
            int yearResult = Integer.valueOf(simpleDateFormat.format(book.getPubDate()));
            System.out.println(yearResult);
            if (year != yearResult) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBookByIdentifier method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBookByIdentifier() {
        System.out.println("findBookByIdentifier");
        String code = "978-0930289232";

        Books expResult = instance.findBookByID(1);
        Books result = instance.findBookByIdentifier(code);
        assertEquals(expResult, result);
    }

    /**
     * Test of findBooksByContributorName method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByContributorName() {
        System.out.println("findBooksByContributorName");
        String name = "Alan Moore";

        List<Books> result = instance.findBooksByContributorName(name);
        boolean working = true;
        for (Books book : result) {
            boolean found = false;

            for (Contributor contributor : book.getContributorList()) {
                System.out.println(contributor.getName());
                if ((contributor.getName()).equals(name)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBooksByFormat method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByFormat() {
        System.out.println("findBooksByFormat");
        String format = "MOBI";

        List<Books> result = instance.findBooksByFormat(format);
        boolean working = true;
        for (Books book : result) {
            boolean found = false;

            for (BookFormats bookFormat : book.getBookFormatsList()) {
                System.out.println(bookFormat.getFormat1().getType());
                if ((bookFormat.getFormat1().getType()).equals(format)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findBooksByGenre method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindBooksByGenre() {
        System.out.println("findBooksByGenre");
        String genre = "Cookbook";

        List<Books> result = instance.findBooksByGenre(genre);
        boolean working = true;
        for (Books book : result) {
            boolean found = false;

            for (Genre genreResult : book.getGenreList()) {
                System.out.println(genreResult.getType());
                if ((genreResult.getType()).equals(genre)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                working = false;
                break;
            }
        }
        assertEquals(true, working);
    }

    /**
     * Test of findNewestBooks method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindNewestBooks() {
        System.out.println("findNewestBooks");
        int amount = 5;

        List<Books> result = instance.findNewestBooks(amount);
        for (Books book : result) {
            System.out.println(book.getPubDate().toString());
        }
        assertEquals(amount, result.size());
    }

    /**
     * Test of findNewestBooks method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testFindRecentlyAddedBooks() {
        System.out.println("findNewestBooks");
        int amount = 5;

        List<Books> result = instance.findRecentlyAddedBooks(amount);
        for (Books book : result) {
            System.out.println(book.getDateEntered().toString());
        }
        assertEquals(amount, result.size());
    }

    /**
     * Test of findBestSellingBook method, of class BooksJpaController.
     */
    @Test //STILL NOT WORKING
    @Ignore
    public void testFindBestSellingBook() {
        System.out.println("findBestSellingBook");
        int amount = 5;

        List<Books> result = instance.findBestSellingBook(amount);
        for (Books book : result) {
            System.out.println(book.getId());
        }
        assertEquals(amount, result.size());
    }

//    /**
//     * Test of findBooksByRating method, of class BooksJpaController.
//     */
//    @Test
//    public void testFindBooksByRating() {
//        System.out.println("findBooksByRating");
//        int rating = 10;
//
//        List<Books> result = instance.findBooksByRating(rating);
//        assertEquals(20, result.size());
//    }

    /**
     * Test of updateRemovalStatus method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testUpdateRemovalStatus() throws Exception {
        System.out.println("updateRemovalStatus");
        boolean status = true;
        int bookID = 1;

        instance.updateRemovalStatus(status, bookID);
        
        Books result = instance.findBookByID(bookID);
        
        assertEquals(status, result.getRemovalStatus());
    }

    /**
     * Test of bookExists method, of class BooksJpaController.
     */
    @Test
    @Ignore
    public void testBookExists() {
        System.out.println("bookExists");
        int id = 1;

        boolean expResult = true;
        boolean result = instance.bookExists(id);
        assertEquals(expResult, result);
    }

    private Books createMockBook() throws Exception {
        Books book = new Books();
        List<Contributor> contributorList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();
        List<BookFormats> bookFormatsList = new ArrayList<>();
        List<BookIdentifiers> bookIdentifiersList = new ArrayList<>();

        book.setTitle("The Book of Lies");
        book.setPublisher("Lies Deparment");
        book.setDescription("Book containing all the most believable lies");
        book.setPages(666);
        book.setWholesalePrice(BigDecimal.valueOf(45.00));
        book.setListPrice(BigDecimal.valueOf(200.00));
        //book.setSalePrice(BigDecimal.valueOf(60.00));
        book.setPubDate(Date.from(Instant.now()));
        //book.setRemovalStatus(false);
        //book.setDateEntered(Date.from(Instant.now()));

        //Set Contributors
        Contributor contributor = new Contributor();
        contributor.setName("Ofer Nitka-Nakash");
        contributor.setContribution(new ContributionType("Author"));
        contributorList.add(contributor);
        book.setContributorList(contributorList);

        //Set Genres
        genreList.add(new Genre("Horror"));
        genreList.add(new Genre("Comedy"));
        book.setGenreList(genreList);

        //Set Formats
        BookFormatsPK pk = new BookFormatsPK(0, "PDF");
        BookFormats bookformat = new BookFormats(pk);
        //bookformat.setFormat1(new Format("PDF"));
        bookformat.setFile("bookOfLies.pdf");
        bookFormatsList.add(bookformat);
        book.setBookFormatsList(bookFormatsList);

        //Set Identifiers
        BookIdentifiersPK pk2 = new BookIdentifiersPK(0, "ISBN-13");
        BookIdentifiers bookIdentifier = new BookIdentifiers(pk2);
        bookIdentifier.setCode("978-6666666666");
        //bookIdentifier.setIdentifierType(new IdentifierType("ISBN-13"));
        bookIdentifiersList.add(bookIdentifier);
        book.setBookIdentifiersList(bookIdentifiersList);

        instance.create(book);
        System.out.println("The book id: " + book.getId());

        return book;
    }

    private Books createIncompleteMockBook() throws Exception {
        Books book = new Books();
        List<Contributor> contributorList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();
        List<BookFormats> bookFormatsList = new ArrayList<>();
        List<BookIdentifiers> bookIdentifiersList = new ArrayList<>();

        book.setTitle("The Book of Lies");
        book.setPublisher("Lies Deparment");
        book.setDescription("Book containing all the most believable lies");
        book.setPages(666);
        book.setWholesalePrice(BigDecimal.valueOf(45.00));
        book.setListPrice(BigDecimal.valueOf(200.00));
        //book.setSalePrice(BigDecimal.valueOf(60.00));
        book.setPubDate(Date.from(Instant.now()));
        //book.setRemovalStatus(false);
        //book.setDateEntered(Date.from(Instant.now()));

//        //Set Contributors
//        Contributor contributor = new Contributor();
//        contributor.setName("Ofer Nitka-Nakash");
//        contributor.setContribution(new ContributionType("Author"));
//        contributorList.add(contributor);
//        book.setContributorList(contributorList);
//
//        //Set Genres
//        genreList.add(new Genre("Horror"));
//        genreList.add(new Genre("Comedy"));
//        book.setGenreList(genreList);
//
//        //Set Formats
//        BookFormatsPK pk = new BookFormatsPK(0, "PDF");
//        BookFormats bookformat = new BookFormats(pk);
//        //bookformat.setFormat1(new Format("PDF"));
//        bookformat.setFile("bookOfLies.pdf");
//        bookFormatsList.add(bookformat);
//        book.setBookFormatsList(bookFormatsList);
//
//        //Set Identifiers
//        BookIdentifiersPK pk2 = new BookIdentifiersPK(0, "ISBN-13");
//        BookIdentifiers bookIdentifier = new BookIdentifiers(pk2);
//        bookIdentifier.setCode("978-6666666666");
//        //bookIdentifier.setIdentifierType(new IdentifierType("ISBN-13"));
//        bookIdentifiersList.add(bookIdentifier);
//        book.setBookIdentifiersList(bookIdentifiersList);
        instance.create(book);
        System.out.println("The book id: " + book.getId());

        return book;
    }
    //-----------------------------------------------------END OF TEST METHODS----------------------------------

    /**
     * This routine is courtesy of Bartosz Majsak who also solved my Arquillian
     * remote server problem
     */
    @Before
    public void seedDatabase() {
        final String seedDataScript = loadAsString("CreateDB.sql");

        try (Connection connection = ds.getConnection()) {
            for (String statement : splitStatements(new StringReader(
                    seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed seeding database", e);
        }
        System.out.println("Seeding works");
    }

    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path)) {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader,
            String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<>();
        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//")
                || line.startsWith("/*");
    }
}
