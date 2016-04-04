/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Admin;
import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Books;
import com.g4w16.entities.Client;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author Annie So
 */
@RunWith(Arquillian.class)
public class SalesJpaControllerTest {

    @Inject
    private SalesJpaController salesController;

    @Inject
    private ClientJpaController clientController;

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
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the date.
     */
    @Test
    public void testCreateMissingDate() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the net value.
     */
    @Test
    public void testCreateMissingNetValue() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the gross value.
     */
    @Test
    public void testCreateMissingGrossValue() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing removed.
     */
    @Test
    public void testCreateMissingRemoved() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the sales details list.
     */
    @Test
    public void testCreateMissingSalesDetailsList() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setBillingAddress(address);
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the billing address.
     */
    @Test(expected = Exception.class)
    public void testCreateMissingBillingAddress() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setClient(client);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the create method missing the client.
     */
    @Test(expected = Exception.class)
    public void testCreateMissingClient() throws Exception {
        int originalSalesCount = salesController.getSalesCount();

        Client client = clientController.findClientById(1);
        BillingAddress address = client.getBillingAddressList().get(0);

        Sales sale = new Sales();

        sale.setDateEntered(new Date());
        sale.setNetValue(BigDecimal.ZERO);
        sale.setGrossValue(BigDecimal.ZERO);
        sale.setRemoved(false);
        sale.setSalesDetailsList(new ArrayList<>());
        sale.setBillingAddress(address);

        salesController.create(sale);

        int newSalesCount = salesController.getSalesCount();
        assertThat(newSalesCount).isGreaterThan(originalSalesCount);
    }

    /**
     * Test the no parameter findSalesEntities method.
     */
    @Test
    public void testFindSalesEntities() {
        List<Sales> allSales = salesController.findSalesEntities();
        int salesCount = salesController.getSalesCount();

        assertThat(allSales).hasSize(salesCount);
    }

    /**
     * Test the findSalesEntites method with arguments.
     */
    @Test
    public void testFindSalesEntitiesWithArgs() {
        int startingResult = 0;
        int numResult = 20;
        List<Sales> retrievedSales = salesController.findSalesEntities(numResult, startingResult);

        assertThat(retrievedSales).hasSize(numResult);
    }

    /**
     * Test the findSales method.
     */
    @Test
    public void testFindSales() {
        Sales sale = salesController.findSales(1);

        assertThat(sale.getId()).isEqualTo(1);
    }

    /**
     * Test the findSales method if the id sent in is not in the database.
     */
    @Test
    public void testFindNonexistentSales() {
        Sales sale = salesController.findSales(Integer.MIN_VALUE);

        assertThat(sale).isNull();
    }

    /**
     * Test the getSalesCount method.
     */
    @Test
    public void testGetSalesCount() {
        int salesCount = salesController.getSalesCount();

        assertThat(salesCount).isEqualTo(150);
    }

    /**
     * Test the removeSales method.
     */
    @Test
    public void testRemoveSales() throws RollbackFailureException, Exception {
        salesController.removeSale(2);
        Sales sale = salesController.findSales(2);

        assertThat(sale.getRemoved()).isEqualTo(true);
    }

    /**
     * Test the findSalesByClientid method.
     */
    @Test
    public void testFindSalesByClientId() {
        List<Sales> sales = salesController.findSalesByClientId(30);

        assertThat(sales).hasSize(4);
    }

    /**
     * Test the findSalesByClientId method if the id is not in the database.
     */
    @Test
    public void testFindSalesByNonexistentClientId() {
        List<Sales> sales = salesController.findSalesByClientId(Integer.MIN_VALUE);

        assertThat(sales).hasSize(0);
    }

    /**
     * Test the findSalesByClientId if the client has not bought anything.
     */
    @Test
    public void testFindSalesByClientIdWithNoSales() {
        List<Sales> sales = salesController.findSalesByClientId(2);

        assertThat(sales).hasSize(0);
    }

    /**
     * Test the getTotalSales method.
     */
    @Test
    public void testGetTotalSales() {
        List<Object[]> saleItems = salesController.getTotalSales(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(saleItems).hasSize(35);
    }

    /**
     * Test the getTotalSales method if there were no sales made in that date
     * range.
     */
    @Test
    public void testGetTotalSalesNoSales() {
        List<Object[]> saleItems = salesController.getTotalSales(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByClient method.
     */
    @Test
    public void testGetSalesByClient() {
        List<Object[]> saleItems = salesController.getSalesByClient(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                100);
        
        assertThat(saleItems).hasSize(4);
    }

    /**
     * Test the getSalesByClient method if the client id is not in the database.
     */
    @Test
    public void testGetSalesByNonexistentClient() {
        List<Object[]> saleItems = salesController.getSalesByClient(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                Integer.MIN_VALUE);
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByClient method if the client has not made any purchases
     * in that date range
     */
    @Test
    public void testGetSalesByClientNoSales() {
        List<Object[]> saleItems = salesController.getSalesByClient(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)),
                50);
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByContributor method.
     */
    @Test
    public void testGetSalesByContributor() {
        List<Object[]> saleItems = salesController.getSalesByContributor(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "Erik Larson");
        
        assertThat(saleItems).hasSize(2);
    }

    /**
     * Test the getSalesByContributor method for a nonexistent contributor.
     */
    @Test
    public void testGetSalesByNonexistentContributor() {
        List<Object[]> saleItems = salesController.getSalesByContributor(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "Some Person");
       
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByContributor method for an existing contributor that
     * has not had any sales in that date range.
     */
    @Test
    public void testGetSalesByContributorNoSales() {
        List<Object[]> saleItems = salesController.getSalesByContributor(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "Erin Moore");
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByPublisher method.
     */
    @Test
    public void testGetSalesByPublisher() {
        List<Object[]> saleItems = salesController.getSalesByPublisher(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "Ten Speed Press");
        
        assertThat(saleItems).hasSize(2);
    }

    /**
     * Test the getSalesByPublisher method for a nonexistent publisher.
     */
    @Test
    public void testGetSalesByNonexistentPublisher() {
        List<Object[]> saleItems = salesController.getSalesByPublisher(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "We don't exist");
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getSalesByPublisher method for a publisher with no sales in that
     * date range.
     */
    @Test
    public void testGetSalesByPublisherNoSales() {
        List<Object[]> saleItems = salesController.getSalesByPublisher(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)),
                "Orbit");
        
        assertThat(saleItems).hasSize(0);
    }

    /**
     * Test the getTopSellers method.
     */
    @Test
    public void testGetTopSellers() {
        List<Object[]> topSellers = salesController.getTopSellers(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(topSellers).hasSize(35);
    }

    /**
     * Test the getTopSellers method if there are no sales in that date range.
     */
    @Test
    public void testGetTopSellersNoSales() {
        List<Object[]> topSellers = salesController.getTopSellers(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(topSellers).hasSize(0);
    }
    
    /**
     * Test the getTopClients method.
     */
    @Test
    public void testGetTopClients() {
        List<Object[]> topClients = salesController.getTopClients(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(topClients).hasSize(19);
    }

    /**
     * Test the getTopClients method if there are no sales in that date range.
     */
    @Test
    public void testGetTopClientsNoSales() {
        List<Object[]> topClients = salesController.getTopClients(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(topClients).hasSize(0);
    }
    
    /**
     * Test the getZeroSales method.
     */
    @Test
    public void testGetZeroSales() {
        List<Object[]> zeroSales = salesController.getZeroSales(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2016, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(zeroSales).hasSize(65);
    }

    /**
     * Test the getZeroSales method if there are no sales in that date range.
     */
    @Test
    public void testGetZeroSalesNoSales() {
        List<Object[]> zeroSales = salesController.getZeroSales(
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 1, 1).toEpochDay() * 24 * 60 * 60)),
                Date.from(Instant.ofEpochSecond(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60)));
        
        assertThat(zeroSales).hasSize(100);
    }
}
