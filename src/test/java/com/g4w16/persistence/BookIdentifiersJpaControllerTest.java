package com.g4w16.persistence;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.g4w16.entities.BookIdentifiers;
import com.g4w16.entities.BookIdentifiersPK;
import com.g4w16.entities.Format;
import com.g4w16.persistence.exceptions.NonexistentEntityException;
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
import javax.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Brandon Balala
 */
@RunWith(Arquillian.class)
//@Ignore
public class BookIdentifiersJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private BookIdentifiersJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public BookIdentifiersJpaControllerTest() {
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
     * Test of create method, of class BookIdentifiersJpaController.
     */
    @Test
    
    public void testCreate() throws Exception {
        System.out.println("create");
        BookIdentifiersPK pk = new BookIdentifiersPK(25, "ISBN-10");
        BookIdentifiers bookIdentifiers = new BookIdentifiers(pk, "TEST");

        instance.create(bookIdentifiers);
        BookIdentifiers result = instance.findBookIdentifierByID(pk);

        assertEquals(bookIdentifiers, result);
    }

    /**
     * Test of create non existent identifier type, of class
     * BookIdentifiersJpaController.
     */
    @Test(expected = NonexistentEntityException.class)
    
    public void testCreateNonExistentIdentifierType() throws Exception {
        System.out.println("create");
        BookIdentifiersPK pk = new BookIdentifiersPK(25, "NEW-IDENTIFIER");
        BookIdentifiers bookIdentifiers = new BookIdentifiers(pk, "TEST");

        instance.create(bookIdentifiers);
        BookIdentifiers result = instance.findBookIdentifierByID(pk);

        assertEquals(bookIdentifiers, result);
    }

    /**
     * Test of create non existent identifier type, of class
     * BookIdentifiersJpaController.
     */
    @Test(expected = NonexistentEntityException.class)
    
    public void testCreateNonExistentBook() throws Exception {
        System.out.println("create");
        BookIdentifiersPK pk = new BookIdentifiersPK(24817, "ISBN-10");
        BookIdentifiers bookIdentifiers = new BookIdentifiers(pk, "TEST");

        instance.create(bookIdentifiers);
    }

    /**
     * Test of create non existent identifier type, of class
     * BookIdentifiersJpaController.
     */
    @Test(expected = ConstraintViolationException.class)
    
    public void testCreateNoCode() throws Exception {
        System.out.println("create");
        BookIdentifiersPK pk = new BookIdentifiersPK(50, "ISBN-10");
        BookIdentifiers bookIdentifiers = new BookIdentifiers(pk);

        instance.create(bookIdentifiers);
    }

    /**
     * Test of edit method, of class BookIdentifiersJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        BookIdentifiersPK pk = new BookIdentifiersPK(25, "ISBN-10");
        BookIdentifiers bookIdentifier = new BookIdentifiers(pk, "TEST");
        instance.create(bookIdentifier);

        bookIdentifier.setCode("CHANGED");
        instance.edit(bookIdentifier);

        BookIdentifiers result = instance.findBookIdentifierByID(pk);

        assertEquals(bookIdentifier.getCode(), result.getCode());
    }

    /**
     * Test of destroy method, of class BookIdentifiersJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");

        BookIdentifiersPK pk = new BookIdentifiersPK(25, "ISBN-10");
        BookIdentifiers bookIdentifier = new BookIdentifiers(pk, "TEST");
        instance.create(bookIdentifier);

        instance.destroy(pk);

        BookIdentifiers result = instance.findBookIdentifierByID(pk);
        assertEquals(null, result);
    }

    /**
     * Test of findAllBookIdentifiers method, of class
     * BookIdentifiersJpaController.
     */
    @Test
    
    public void testFindAllBookIdentifiers() {
        System.out.println("findAllBookIdentifiers");

        int expResult = 100;
        List<BookIdentifiers> result = instance.findAllBookIdentifiers();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBookIdentifiersEntities method, of class
     * BookIdentifiersJpaController.
     */
    @Test
    
    public void testFindBookIdentifiersEntities() {
        System.out.println("findBookIdentifiersEntities");
        int maxResults = 77;
        int firstResult = 15;

        int expResult = 77;
        List<BookIdentifiers> result = instance.findBookIdentifiersEntities(maxResults, firstResult);

        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBookIdentifierByID method, of class
     * BookIdentifiersJpaController.
     */
    @Test
    
    public void testFindBookIdentifierByID() {
        System.out.println("findBookIdentifierByID");
        BookIdentifiersPK pk = new BookIdentifiersPK(25, "ISBN-13");
        BookIdentifiers result = instance.findBookIdentifierByID(pk);

        assertEquals(pk, result.getBookIdentifiersPK());
    }

    /**
     * Test of getBookIdentifiersCount method, of class
     * BookIdentifiersJpaController.
     */
    @Test
        
    public void testGetBookIdentifiersCount() {
        System.out.println("getBookIdentifiersCount");

        int expResult = 100;
        int result = instance.getBookIdentifiersCount();
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
