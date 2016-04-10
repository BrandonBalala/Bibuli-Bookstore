/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Admin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author Ofer Nitka-Nakash
 */
@RunWith(Arquillian.class)
@Ignore
public class AdminJpaControllerTest {
    
      @Inject
    private AdminJpaController adminJpaController;

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
     * Test of create method, of class AdminJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Admin admin = new Admin();
        int originalCount = adminJpaController.getAdminCount();
        admin.setUsername("TestUsername");
        admin.setPassword("TestPassword");
        adminJpaController.create(admin);
        int finalCount = adminJpaController.getAdminCount();
        assertThat(finalCount).isEqualTo(originalCount+1);
    }

    /**
     * Test of edit method, of class AdminJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Admin admin = new Admin();
        int originalCount = adminJpaController.getAdminCount();
        admin.setUsername("TestUsername");
        admin.setPassword("TestPassword");
        adminJpaController.create(admin);
        int finalCount = adminJpaController.getAdminCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        admin.setPassword("NewTestPassword");
        adminJpaController.edit(admin);
       assertThat("TestPassword").isNotEqualTo(admin.getPassword());
    }

    /**
     * Test of destroy method, of class AdminJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Admin admin = new Admin();
        int originalCount = adminJpaController.getAdminCount();
        admin.setUsername("TestUsername");
        admin.setPassword("TestPassword");
        adminJpaController.create(admin);
        int finalCount = adminJpaController.getAdminCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        adminJpaController.destroy(admin.getUsername());
        //get new final count
        finalCount = adminJpaController.getAdminCount();
        assertThat(finalCount).isEqualTo(originalCount);
    }

    /**
     * Test of findAdminEntities method, of class AdminJpaController.
     */
    @Test
    public void testFindAdminEntities_0args() throws Exception {
        System.out.println("findAdminEntities");
        Admin admin = new Admin();
        admin.setUsername("testertesting");
        admin.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin);
        List<Admin> expResult = new ArrayList<Admin>();
        expResult.add(admin);
        List<Admin> result = adminJpaController.findAdminEntities();
        assertEquals(expResult, result);
    }

    /**
     * Test of findAdminEntities method, of class AdminJpaController.
     */
    @Test
    public void testFindAdminEntities_int_int() throws Exception {
        System.out.println("findAdminEntities");
        int maxResults = 2;
        int firstResult = 0;
        Admin admin = new Admin();
        admin.setUsername("testertesting");
        admin.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin);
        Admin admin1 = new Admin();
        admin1.setUsername("testertesting2");
        admin1.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin1);
        Admin admin2 = new Admin();
        admin2.setUsername("testertesting3");
        admin2.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin2);
        List<Admin> expResult = new ArrayList<Admin>();
        expResult.add(admin);
        expResult.add(admin1);
        List<Admin> result = adminJpaController.findAdminEntities(maxResults, firstResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of findAdmin method, of class AdminJpaController.
     */
    @Test
    public void testFindAdmin() throws Exception {
        System.out.println("findAdmin");
        Admin admin = new Admin();
        admin.setUsername("testertesting");
        admin.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin);
        Admin admin1 = new Admin();
        admin1.setUsername("testertesting2");
        admin1.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin1);
        Admin admin2 = new Admin();
        admin2.setUsername("testertesting3");
        admin2.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin2);
        Admin expResult = admin1;
        Admin result = adminJpaController.findAdmin(admin1.getUsername());
        assertEquals(expResult, result);
    }

    /**
     * Test of getAdminCount method, of class AdminJpaController.
     */
    @Test
    public void testGetAdminCount() throws Exception{
        System.out.println("getAdminCount");
        Admin admin = new Admin();
        admin.setUsername("testertesting");
        admin.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin);
        Admin admin1 = new Admin();
        admin1.setUsername("testertesting2");
        admin1.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin1);
        Admin admin2 = new Admin();
        admin2.setUsername("testertesting3");
        admin2.setPassword("MuchGoodSoGreat");
        adminJpaController.create(admin2);
        int expResult = 3;
        int result = adminJpaController.getAdminCount();
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
