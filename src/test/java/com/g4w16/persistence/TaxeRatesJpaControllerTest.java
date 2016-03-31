/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Admin;
import com.g4w16.entities.TaxeRates;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Ignore;

/**
 *
 * @author ofern
 */
@RunWith(Arquillian.class)
@Ignore
public class TaxeRatesJpaControllerTest {
    
    @Inject
    private TaxeRatesJpaController taxeRatesJpaController;
    
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
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
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
     * Test of create method, of class TaxeRatesJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        TaxeRates taxeRate = new TaxeRates();
        int originalCount = taxeRatesJpaController.getTaxeRatesCount();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate);
        int finalCount = taxeRatesJpaController.getTaxeRatesCount();
        assertThat(finalCount).isEqualTo(originalCount+1);
    }

    /**
     * Test of edit method, of class TaxeRatesJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
         TaxeRates taxeRate = new TaxeRates();
        int originalCount = taxeRatesJpaController.getTaxeRatesCount();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate);
        int finalCount = taxeRatesJpaController.getTaxeRatesCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        taxeRate.setHst(BigDecimal.ZERO);
        taxeRatesJpaController.edit(taxeRate);
        assertThat(1).isNotEqualTo(taxeRate.getHst().intValue());
    }

    /**
     * Test of destroy method, of class TaxeRatesJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        TaxeRates taxeRate = new TaxeRates();
        int originalCount = taxeRatesJpaController.getTaxeRatesCount();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate);
        int finalCount = taxeRatesJpaController.getTaxeRatesCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        taxeRatesJpaController.destroy(taxeRate.getProvince());
        finalCount = taxeRatesJpaController.getTaxeRatesCount();
        assertThat(finalCount).isEqualTo(originalCount);
    }

    /**
     * Test of findTaxeRatesEntities method, of class TaxeRatesJpaController.
     */
    @Test
    public void testFindTaxeRatesEntities_0args() throws Exception{
        System.out.println("findTaxeRatesEntities");
        TaxeRates taxeRate = new TaxeRates();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate); 
        List<TaxeRates> expResult = new ArrayList<TaxeRates>();
        expResult.add(taxeRate);
        List<TaxeRates> result = taxeRatesJpaController.findTaxeRatesEntities();
        assertEquals(expResult, result);
    }

    /**
     * Test of findTaxeRatesEntities method, of class TaxeRatesJpaController.
     */
    @Test
    public void testFindTaxeRatesEntities_int_int() throws Exception{
        System.out.println("findTaxeRatesEntities");
        int maxResults = 2;
        int firstResult = 0;
        TaxeRates taxeRate = new TaxeRates();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate); 
        TaxeRates taxeRate1 = new TaxeRates();
        taxeRate1.setProvince("QC");
        taxeRate1.setHst(BigDecimal.ONE);
        taxeRate1.setPst(BigDecimal.ONE);
        taxeRate1.setGst(BigDecimal.ONE);
        taxeRate1.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate1);
        TaxeRates taxeRate2 = new TaxeRates();
        taxeRate2.setProvince("AB");
        taxeRate2.setHst(BigDecimal.ONE);
        taxeRate2.setPst(BigDecimal.ONE);
        taxeRate2.setGst(BigDecimal.ONE);
        taxeRate2.setUpdated(Date.from(Instant.now()));
        List<TaxeRates> expResult = new ArrayList<TaxeRates>();
        expResult.add(taxeRate);
        expResult.add(taxeRate1);
        List<TaxeRates> result = taxeRatesJpaController.findTaxeRatesEntities(maxResults, firstResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of findTaxeRates method, of class TaxeRatesJpaController.
     */
    @Test
    public void testFindTaxeRates() throws Exception{
        System.out.println("findTaxeRates");
        TaxeRates taxeRate = new TaxeRates();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate); 
        TaxeRates taxeRate1 = new TaxeRates();
        taxeRate1.setProvince("QC");
        taxeRate1.setHst(BigDecimal.ONE);
        taxeRate1.setPst(BigDecimal.ONE);
        taxeRate1.setGst(BigDecimal.ONE);
        taxeRate1.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate1);
        TaxeRates taxeRate2 = new TaxeRates();
        taxeRate2.setProvince("AB");
        taxeRate2.setHst(BigDecimal.ONE);
        taxeRate2.setPst(BigDecimal.ONE);
        taxeRate2.setGst(BigDecimal.ONE);
        taxeRate2.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate2);
        TaxeRates expResult = taxeRate2;
        TaxeRates result = taxeRatesJpaController.findTaxeRates(taxeRate2.getProvince());
        assertEquals(expResult, result);
    }

    /**
     * Test of getTaxeRatesCount method, of class TaxeRatesJpaController.
     */
    @Test
    public void testGetTaxeRatesCount() throws Exception{
        System.out.println("getTaxeRatesCount");
        TaxeRates taxeRate = new TaxeRates();
        taxeRate.setProvince("ON");
        taxeRate.setHst(BigDecimal.ONE);
        taxeRate.setPst(BigDecimal.ONE);
        taxeRate.setGst(BigDecimal.ONE);
        taxeRate.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate); 
        TaxeRates taxeRate1 = new TaxeRates();
        taxeRate1.setProvince("QC");
        taxeRate1.setHst(BigDecimal.ONE);
        taxeRate1.setPst(BigDecimal.ONE);
        taxeRate1.setGst(BigDecimal.ONE);
        taxeRate1.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate1);
        TaxeRates taxeRate2 = new TaxeRates();
        taxeRate2.setProvince("AB");
        taxeRate2.setHst(BigDecimal.ONE);
        taxeRate2.setPst(BigDecimal.ONE);
        taxeRate2.setGst(BigDecimal.ONE);
        taxeRate2.setUpdated(Date.from(Instant.now()));
        taxeRatesJpaController.create(taxeRate2);
        int expResult = 3;
        int result = taxeRatesJpaController.getTaxeRatesCount();
        assertEquals(expResult, result);
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
