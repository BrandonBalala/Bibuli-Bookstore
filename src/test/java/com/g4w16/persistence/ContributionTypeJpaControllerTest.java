package com.g4w16.persistence;



import com.g4w16.entities.ContributionType;
import com.g4w16.entities.Format;
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
public class ContributionTypeJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private ContributionTypeJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public ContributionTypeJpaControllerTest() {
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
     * Test of create method, of class ContributionTypeJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        ContributionType contributionType = new ContributionType("TEST");

        instance.create(contributionType);

        ContributionType retrievedContributionType = instance.findContributionTypeByID(contributionType.getType());
        assertEquals(contributionType, retrievedContributionType);
    }

    /**
     * Test duplicate, of class ContributionTypeJpaController.
     */
    @Test(expected = RollbackFailureException.class)
    public void testDuplicateCreate() throws Exception {
        System.out.println("create");
        ContributionType contributionType = new ContributionType("TEST");

        instance.create(contributionType);
        instance.create(contributionType);
    }

    /**
     * Test of destroy method, of class ContributionTypeJpaController.
     */
    @Test(expected = IllegalOrphanException.class)
    public void testDestroyUsedContributionType() throws Exception {
        System.out.println("destroy");
        String id = "Author";

        instance.destroy(id);

        ContributionType retrievedContributionType = instance.findContributionTypeByID(id);

        assertEquals(null, retrievedContributionType);
    }

    /**
     * Test of destroy method, of class ContributionTypeJpaController.
     */
    @Test
    public void testDestroyNotUsedContributionType() throws Exception {
        System.out.println("destroy");
        ContributionType contributionType = new ContributionType("TEST");

        instance.create(contributionType);
        instance.destroy(contributionType.getType());

        ContributionType retrievedContributionType = instance.findContributionTypeByID(contributionType.getType());

        assertEquals(null, retrievedContributionType);
    }

    /**
     * Test of findAllContributionTypes method, of class
     * ContributionTypeJpaController.
     */
    @Test
    public void testFindAllContributionTypes() {
        System.out.println("findAllContributionTypes");

        int expResult = 5;
        List<ContributionType> result = instance.findAllContributionTypes();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findAllContributionTypes method, of class
     * ContributionTypeJpaController.
     */
    @Test
    public void testFindAllContributionTypesAddingOne() throws Exception {
        System.out.println("findAllContributionTypes");
        int expResult = 6;
        
        ContributionType contributionType = new ContributionType("TEST");

        instance.create(contributionType);
        List<ContributionType> result = instance.findAllContributionTypes();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findContributionTypeEntities method, of class
     * ContributionTypeJpaController.
     */
    @Test
    public void testFindContributionTypeEntities() {
        System.out.println("findContributionTypeEntities");
        int maxResults = 3;
        int firstResult = 4;

        int expResult = 1;
        List<ContributionType> result = instance.findContributionTypeEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findContributionTypeByID method, of class
     * ContributionTypeJpaController.
     */
    @Test
    public void testFindContributionTypeByID() {
        System.out.println("findContributionTypeByID");
        String id = "";

        ContributionType expResult = null;
        ContributionType result = instance.findContributionTypeByID(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of getContributionTypeCount method, of class
     * ContributionTypeJpaController.
     */
    @Test
    public void testGetContributionTypeCount() {
        System.out.println("getContributionTypeCount");

        int expResult = 5;
        int result = instance.getContributionTypeCount();
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
