package com.g4w16.persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Contributor;
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
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author BRANDON-PC
 */
@RunWith(Arquillian.class)
//@Ignore
public class ContributorJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private ContributorJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public ContributorJpaControllerTest() {
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
     * Test of create method, of class ContributorJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Author"));

        instance.create(contributor);

        Contributor result = instance.findContributorByID(contributor.getId());

        assertEquals(contributor, result);
    }

    @Test(expected = NonexistentEntityException.class)
    public void testCreateNonExistentContributionType() throws Exception {
        System.out.println("testCreateNonExistentContributionType");
        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Programmer"));

        instance.create(contributor);
    }

    @Test(expected = NonexistentEntityException.class)
    public void testCreateNoName() throws Exception {
        System.out.println("testCreateNonExistentContributionType");
        Contributor contributor = new Contributor();
        contributor.setContribution(new ContributionType("Programmer"));

        instance.create(contributor);
    }

    /**
     * Test of edit method, of class ContributorJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Author"));
        instance.create(contributor);

        contributor.setName("CHANGED NAME");
        instance.edit(contributor);

        Contributor result = instance.findContributorByID(contributor.getId());

        assertEquals(contributor.getName(), result.getName());
    }

    /**
     * Test of destroy method, of class ContributorJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Contributor contributor = new Contributor();
        contributor.setName("Brandon Balala");
        contributor.setContribution(new ContributionType("Author"));
        instance.create(contributor);

        instance.destroy(contributor.getId());

        Contributor result = instance.findContributorByID(contributor.getId());
        
        assertEquals(null, result);
    }

    /**
     * Test of findAllContributors method, of class ContributorJpaController.
     */
    @Test
    public void testFindAllContributors() {
        System.out.println("findAllContributors");

        int expResult = 95;
        List<Contributor> result = instance.findAllContributors();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findContributorEntities method, of class
     * ContributorJpaController.
     */
    @Test
    public void testFindContributorEntities() {
        System.out.println("findContributorEntities");
        int maxResults = 35;
        int firstResult = 22;

        int expResult = 35;
        List<Contributor> result = instance.findContributorEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findContributorByID method, of class ContributorJpaController.
     */
    @Test
    public void testFindContributorByID() {
        System.out.println("findContributorByID");
        Integer id = null;

        Contributor expResult = new Contributor(95, "William Tucker");
        expResult.setContribution(new ContributionType("Author"));
        
        Contributor result = instance.findContributorByID(expResult.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of getContributorCount method, of class ContributorJpaController.
     */
    @Test
    public void testGetContributorCount() {
        System.out.println("getContributorCount");

        int expResult = 95;
        int result = instance.getContributorCount();
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
