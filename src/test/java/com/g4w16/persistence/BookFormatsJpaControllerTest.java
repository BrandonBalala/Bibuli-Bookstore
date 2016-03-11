/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.BookFormats;
import com.g4w16.entities.BookFormatsPK;
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
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author BRANDON-PC
 */
@RunWith(Arquillian.class)
//@Ignore
public class BookFormatsJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private BookFormatsJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public BookFormatsJpaControllerTest() {
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
     * Test of create method, of class BookFormatsJpaController.
     */
    @Test @Ignore
    public void testCreate() throws Exception {
        System.out.println("create");
        BookFormatsPK pk = new BookFormatsPK(1, "PDB");
        BookFormats bookformats = new BookFormats(pk);
        bookformats.setFile("testFile.pdb");

        instance.create(bookformats);
        BookFormats result = instance.findBookFormatByID(pk);

        assertEquals(bookformats, result);
    }

    /**
     * Test of create method, of class BookFormatsJpaController.
     */
    @Test @Ignore//(expected = NonexistentEntityException.class)
    public void testCreateNonExistentFormat() throws Exception {
        System.out.println("testCreateNonExistentFormat");
        BookFormatsPK pk = new BookFormatsPK(1, "RANDOM_FORMAT");
        BookFormats bookformats = new BookFormats(pk);
        bookformats.setFile("testFile.pdb");

        instance.create(bookformats);
    }

    /**
     * Test of create method, of class BookFormatsJpaController.
     */
    @Test @Ignore//(expected = ConstraintViolationException.class)
    public void testCreateNoFile() throws Exception {
        System.out.println("testCreateNonExistentFormat");
        BookFormatsPK pk = new BookFormatsPK(1, "PDB");
        BookFormats bookformats = new BookFormats(pk);

        instance.create(bookformats);
    }

    /**
     * Test of edit method, of class BookFormatsJpaController.
     */
    @Test @Ignore
    public void testEdit() throws Exception {
        System.out.println("edit");
        BookFormatsPK pk = new BookFormatsPK(1, "PDB");
        BookFormats bookformats = new BookFormats(pk);
        bookformats.setFile("testFile.pdb");
        instance.create(bookformats);

        bookformats.setFile("CHANGED.PDB");
        instance.edit(bookformats);

        BookFormats result = instance.findBookFormatByID(pk);

        assertEquals(bookformats.getFile(), result.getFile());
    }

    /**
     * Test of destroy method, of class BookFormatsJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        BookFormatsPK pk = new BookFormatsPK(1, "PDB");
        BookFormats bookformats = new BookFormats(pk);
        bookformats.setFile("testFile.pdb");
        instance.create(bookformats);

        instance.destroy(pk);

        BookFormats result = instance.findBookFormatByID(pk);

        assertEquals(null, result);
    }

    /**
     * Test of findAllBookFormats method, of class BookFormatsJpaController.
     */
    @Test @Ignore
    public void testFindAllBookFormats() {
        System.out.println("findAllBookFormats");

        int expResult = 129;
        List<BookFormats> result = instance.findAllBookFormats();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBookFormatsEntities method, of class
     * BookFormatsJpaController.
     */
    @Test @Ignore
    public void testFindBookFormatsEntities() {
        System.out.println("findBookFormatsEntities");
        int maxResults = 99;
        int firstResult = 30;

        int expResult = 99;
        List<BookFormats> result = instance.findBookFormatsEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findBookFormatByID method, of class BookFormatsJpaController.
     */
    @Test @Ignore
    public void testFindBookFormatByID() {
        System.out.println("findBookFormatByID");
        BookFormatsPK pk = new BookFormatsPK(1, "PDF");

        BookFormats result = instance.findBookFormatByID(pk);
        assertEquals(pk, result.getBookFormatsPK());
    }

    /**
     * Test of getBookFormatsCount method, of class BookFormatsJpaController.
     */
    @Test @Ignore
    public void testGetBookFormatsCount() {
        System.out.println("getBookFormatsCount");

        int expResult = 129;
        int result = instance.getBookFormatsCount();
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
