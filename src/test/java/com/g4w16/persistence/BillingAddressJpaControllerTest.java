/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.BillingAddress;
import com.g4w16.entities.Client;
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
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author Dan 2016/3/1
 */
//@Ignore
@RunWith(Arquillian.class)
public class BillingAddressJpaControllerTest {
    
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
                .addPackage(BillingAddressJpaController.class.getPackage())
                .addPackage(BillingAddress.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    @Inject
    private BillingAddressJpaController billingAddressJpaController;
    
    @Inject
    private ClientJpaController clientJpaController;
    
    /**
     * Test of create method, of class BillingAddressJpaController.
     */
    @Test
    public void testCreate() throws RollbackFailureException, Exception {
        System.out.println("create");
        BillingAddress billingAddress = new BillingAddress();
        Client client=clientJpaController.findClientById(1);
        System.out.println(client);
        billingAddress.setClient(client);
        billingAddress.setName("name");
        billingAddress.setSecondCivicAddress("secondCivicAddress");
        billingAddress.setCity("AA");
        billingAddress.setFirstCivicAddress("12kfngm");
        billingAddress.setPostalCode("A1A1A1");
        billingAddress.setProvince("QC");
        
        billingAddressJpaController.create(billingAddress);
        int result = billingAddressJpaController.getBillingAddressCount();
        assertThat(result).isEqualTo(101);
    }

    /**
     * Test of edit method, of class BillingAddressJpaController.
     */
    @Ignore
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        BillingAddress billingAddress = billingAddressJpaController.findBillingAddressById(3);
        billingAddress.setProvince("ON");
        billingAddressJpaController.edit(billingAddress);
        BillingAddress new_billingAddress = billingAddressJpaController.findBillingAddressById(3);
        assertThat(new_billingAddress.getName()).isEqualTo("ON");
    }

    /**
     * Test of destroy method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = 2;
        billingAddressJpaController.destroy(id);
        int result = billingAddressJpaController.getBillingAddressCount();
        assertThat(result).isEqualTo(99);
    }


    /**
     * Test of findBillingAddressEntities method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressEntities_int_int() {
        System.out.println("findBillingAddressEntities");
        int maxResults = 10;
        int firstResult = 0;
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressEntities(maxResults, firstResult);
        assertThat(expResult).hasSize(10);
    }


    /**
     * Test of getBillingAddressCount method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testGetBillingAddressCount() {
        System.out.println("getBillingAddressCount");
        int result = billingAddressJpaController.getBillingAddressCount();
        assertThat(result).isEqualTo(100);
    }

    /**
     * Test of findAllBillingAddress method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindAllBillingAddress() {
        System.out.println("findAllBillingAddress");
        List<BillingAddress> expResult = billingAddressJpaController.findAllBillingAddress();
        assertThat(expResult).hasSize(100);
    }

    /**
     * Test of findBillingAddressById method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressById() {
        System.out.println("findBillingAddressById");
        int id = 1;
        BillingAddress expResult = billingAddressJpaController.findBillingAddressById(id);
        assertThat(expResult.getId()).isEqualTo(id);
    }

    /**
     * Test of findBillingAddressByName method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressByName() {
        System.out.println("findBillingAddressByName");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressByName("Home");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findBillingAddressById method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressByProvince() {
        System.out.println("findBillingAddressByProvince");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressByProvince("SK");
        assertThat(expResult).hasSize(7);
    }

    /**
     * Test of findBillingAddressByCity method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressByCity() {
        System.out.println("findBillingAddressByCity");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressByCity("Lamont");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findBillingAddressByFirstAddress method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressByFirstAddress() {
        System.out.println("findBillingAddressByFirstAddress");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressByFirstAddress("2 Upham Center");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findBillingAddressBySecondCivicAddress method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressBySecondCivicAddress() {
        System.out.println("findBillingAddressBySecondCivicAddress");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressBySecondCivicAddress("101");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findBillingAddressByPostalCode method, of class BillingAddressJpaController.
     */
    //@Ignore
    @Test
    public void testFindBillingAddressByPostalCode() {
        System.out.println("findBillingAddressByPostalCode");
        List<BillingAddress> expResult = billingAddressJpaController.findBillingAddressByPostalCode("h3n5m8");
        assertThat(expResult).hasSize(1);
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

