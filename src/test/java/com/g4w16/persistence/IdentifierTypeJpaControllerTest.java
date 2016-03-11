package com.g4w16.persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.g4w16.entities.Format;
import com.g4w16.entities.IdentifierType;
import com.g4w16.persistence.exceptions.IllegalOrphanException;
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
public class IdentifierTypeJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private IdentifierTypeJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public IdentifierTypeJpaControllerTest() {
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
                .addPackage(FormatJpaController.class.getPackage())
                .addPackage(Format.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }

    /**
     * Test of create method, of class IdentifierTypeJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        IdentifierType identifierType = new IdentifierType("TEST");

        instance.create(identifierType);

        IdentifierType retrievedIdentifierType = instance.findIdentifierTypeByID(identifierType.getType());
        assertEquals(identifierType, retrievedIdentifierType);
    }

    /**
     * Test duplicates, of class IdentifierTypeJpaController.
     */
    @Test(expected = RollbackFailureException.class)
    public void testDuplicateCreate() throws Exception {
        System.out.println("create");
        IdentifierType identifierType = new IdentifierType("TEST");

        instance.create(identifierType);
        instance.create(identifierType);
    }

    /**
     * Test of destroy method, of class IdentifierTypeJpaController.
     */
    @Test(expected = IllegalOrphanException.class)
    public void testDestroyUsedIdentifierType() throws Exception {
        System.out.println("destroy");
        String id = "ISBN-13";

        instance.destroy(id);

        IdentifierType retrievedIdentifierType = instance.findIdentifierTypeByID(id);

        assertEquals(null, retrievedIdentifierType);
    }

    /**
     * Test of destroy method, of class IdentifierTypeJpaController.
     */
    @Test
    public void testDestroyNotUsedIdentifierType() throws Exception {
        System.out.println("destroy");
        IdentifierType identifierType = new IdentifierType("TEST");

        instance.create(identifierType);
        instance.destroy(identifierType.getType());

        IdentifierType retrievedIdentifierType = instance.findIdentifierTypeByID(identifierType.getType());

        assertEquals(null, retrievedIdentifierType);
    }

    /**
     * Test of findAllIdentifierTypes method, of class
     * IdentifierTypeJpaController.
     */
    @Test
    public void testFindAllIdentifierTypes() {
        System.out.println("findAllIdentifierTypes");

        int expResult = 3;
        List<IdentifierType> result = instance.findAllIdentifierTypes();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findAllIdentifierTypes method, of class
     * IdentifierTypeJpaController.
     */
    @Test
    public void testFindAllIdentifierTypesAddingOne() throws Exception{
        System.out.println("testFindAllIdentifierTypesAddingOne");

        IdentifierType identifierType = new IdentifierType("TEST");
        instance.create(identifierType);
        
        int expResult = 4;
        List<IdentifierType> result = instance.findAllIdentifierTypes();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findIdentifierTypeEntities method, of class
     * IdentifierTypeJpaController.
     */
    @Test
    public void testFindIdentifierTypeEntities() {
        System.out.println("findIdentifierTypeEntities");
        int maxResults = 2;
        int firstResult = 1;

        int expResult = 2;
        List<IdentifierType> result = instance.findIdentifierTypeEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findIdentifierTypeByID method, of class
     * IdentifierTypeJpaController.
     */
    @Test
    public void testFindIdentifierTypeByID() {
        System.out.println("findIdentifierTypeByID");
        String expResult = "ISBN-10";

        IdentifierType result = instance.findIdentifierTypeByID(expResult);
        assertEquals(expResult, result.getType());
    }

    /**
     * Test of getIdentifierTypeCont method, of class
     * IdentifierTypeJpaController.
     */
    @Test
    public void testGetIdentifierTypeCont() {
        System.out.println("getIdentifierTypeCont");

        int expResult = 3;
        int result = instance.getIdentifierTypeCont();
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
