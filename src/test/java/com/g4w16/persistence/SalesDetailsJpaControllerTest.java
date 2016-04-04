/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Admin;
import com.g4w16.entities.Books;
import com.g4w16.entities.Sales;
import com.g4w16.entities.SalesDetails;
import com.g4w16.entities.TaxeRates;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.runner.RunWith;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Ignore;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Annie So
 */
@RunWith(Arquillian.class)
public class SalesDetailsJpaControllerTest {

    @Inject
    private SalesDetailsJpaController salesDetailsController;

    @Inject
    private SalesJpaController salesController;
    @Inject
    private BooksJpaController booksController;
    @Inject
    private TaxeRatesJpaController taxController;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    @Deployment
    public static WebArchive deploy() {
        // Use an alternative to the JUnit assert library called AssertJ
        // Need to reference MySQL driver as it is not part of either embedded or remote TomEE
        final File[] dependencies = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("mysql:mysql-connector-java",
                        "org.assertj:assertj-core").withoutTransitivity()
                .asFile();

        // For testing Arquillian prefers a resources.xml file over acontext.xml
        // Actual file name is resources-mysql-ds.xml in the test/resources folder
        // The SQL script to create the database is also in this folder
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                //.setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addPackage(AdminJpaController.class.getPackage())
                .addPackage(Admin.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }

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

    /**
     * Test the create method.
     */
    @Test
    public void testCreate() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the sale.
     */
    @Test(expected = Exception.class)
    public void testCreateMissingSale() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the book.
     */
    @Test(expected = Exception.class)
    public void testCreateMissingBook() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setSale(sale);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the price.
     */
    @Test(expected = Exception.class)
    public void testCreateMissingPrice() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the GST.
     */
    @Test
    public void testCreateMissingGST() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the PST.
     */
    @Test
    public void testCreateMissingPST() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));

        details.setRemoved(false);
        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method missing the HST.
     */
    @Test
    public void testCreateMissingHST() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setRemoved(false);
        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the create method.
     */
    @Test
    public void testCreateMissingRemoved() throws Exception {
        int originalDetailsCount = salesDetailsController.getSalesDetailsCount();

        Sales sale = salesController.findSales(1);
        Books book = booksController.findBookByID(1);

        SalesDetails details = new SalesDetails();
        details.setPrice(book.getSalePrice());

        String province = sale.getBillingAddress().getProvince();
        TaxeRates taxRate = taxController.getTaxeRateByProvince(province);
        details.setGst(details.getPrice().multiply(taxRate.getGst()));
        details.setHst(details.getPrice().multiply(taxRate.getHst()));
        details.setPst(details.getPrice().multiply(taxRate.getPst()));

        details.setSale(sale);
        details.setBook(book);

        salesDetailsController.create(details);

        int newDetailsCount = salesDetailsController.getSalesDetailsCount();
        assertThat(newDetailsCount).isGreaterThan(originalDetailsCount);
    }

    /**
     * Test the no parameter findSalesDetailsEntities method.
     */
    @Test
    public void testFindSalesDetailsEntities() throws Exception {
        List<SalesDetails> allSalesDetails = salesDetailsController.findSalesDetailsEntities();
        int detailsCount = salesDetailsController.getSalesDetailsCount();

        assertThat(allSalesDetails).hasSize(detailsCount);
    }

    /**
     * Test the findSalesDetailsEntities method with arguments.
     */
    @Test
    public void testFindSalesDetailsEntitiesWithArgs() throws Exception {
        int startingResult = 0;
        int numResults = 20;
        List<SalesDetails> retrievedDetails = salesDetailsController.findSalesDetailsEntities(numResults, startingResult);

        assertThat(retrievedDetails).hasSize(numResults);
    }

    /**
     * Test the findSalesDetails method.
     */
    @Test
    public void testFindSalesDetails() {
        SalesDetails details = salesDetailsController.findSalesDetails(1);

        assertThat(details.getId()).isEqualTo(1);
    }

    /**
     * Test the findSalesDetails method if the id sent in is not in the
     * database.
     */
    @Test
    public void testFindNonexistentSalesDetails() {
        SalesDetails details = salesDetailsController.findSalesDetails(Integer.MIN_VALUE);

        assertThat(details).isNull();
    }

    /**
     * Test the getSalesDetailsCount method.
     */
    @Test
    public void testGetSalesDetailsCount() {
        int detailsCount = salesDetailsController.getSalesDetailsCount();

        assertThat(detailsCount).isEqualTo(500);
    }

    /**
     * Test the removeSalesDetail method.
     */
    @Test
    public void testRemoveSalesDetail() throws RollbackFailureException, Exception {
        salesDetailsController.removeSalesDetail(2);
        SalesDetails details = salesDetailsController.findSalesDetails(2);

        assertThat(details.getRemoved()).isEqualTo(true);
    }

    /**
     * Test the ownsBook method if the client owns the book.
     */
    @Test
    public void testOwnsBookTrue() {
        boolean owns = salesDetailsController.ownsBook(20, 30);

        assertThat(owns).isEqualTo(true);
    }

    /**
     * Test the ownsBook method if the client does not own the book.
     */
    @Test
    public void testOwnsBookFalse() {
        boolean owns = salesDetailsController.ownsBook(20, 50);

        assertThat(owns).isEqualTo(false);
    }

    /**
     * Test the ownsBook method if the client used to own the book but no longer
     * does.
     */
    @Test
    public void testOwnsBookRemoved() {
        boolean owns = salesDetailsController.ownsBook(20, 80);

        assertThat(owns).isEqualTo(false);
    }
    
    /**
     * Test the findBooksOwnedByClientId method.
     */
    @Test
    public void testFindBooksOwnedByClientId(){
        List<Books> booksOwned = salesDetailsController.findBooksOwnedByClientId(30);
        
        assertThat(booksOwned).hasSize(11);
    }
}
