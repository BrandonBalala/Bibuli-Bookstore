/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Format;
import com.g4w16.entities.Poll;
import com.g4w16.persistence.exceptions.RollbackFailureException;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.resAuthType;
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
public class PollJpaControllerTest {
    //private Logger logger = Logger.getLogger(JPATesterTest.class.getName());

    @Inject
    private PollJpaController instance;

    @Resource(name = "java:app/jdbc/g4w16")
    private DataSource ds;

    public PollJpaControllerTest() {
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
     * Test of create method, of class PollJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Poll poll = new Poll();
        poll.setQuestion("Do you like testing?");
        poll.setFirstAnswer("Yes");
        poll.setSecondAnswer("No");
        poll.setThirdAnswer("Not really");
        poll.setFourthAnswer("NOOOOOOOO!");

        instance.create(poll);

        Poll result = instance.findPollByID(poll.getId());

        assertEquals(poll, result);
    }

    /**
     * Test of create method, of class PollJpaController.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testCreateNoQuestion() throws Exception {
        System.out.println("create");
        Poll poll = new Poll();
        //poll.setQuestion("Do you like testing?");
        poll.setFirstAnswer("Yes");
        poll.setSecondAnswer("No");
        poll.setThirdAnswer("Not really");
        poll.setFourthAnswer("NOOOOOOOO!");

        instance.create(poll);
    }

    /**
     * Test of edit method, of class PollJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Poll poll = new Poll();
        poll.setQuestion("Do you like testing?");
        poll.setFirstAnswer("Yes");
        poll.setSecondAnswer("No");
        poll.setThirdAnswer("Not really");
        poll.setFourthAnswer("NOOOOOOOO!");

        instance.create(poll);

        poll.setFirstCount(100);
        instance.edit(poll);

        Poll result = instance.findPollByID(poll.getId());

        assertEquals(poll.getFirstCount(), result.getFirstCount());
    }

    /**
     * Test of destroy method, of class PollJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Poll poll = new Poll();
        poll.setQuestion("Do you like testing?");
        poll.setFirstAnswer("Yes");
        poll.setSecondAnswer("No");
        poll.setThirdAnswer("Not really");
        poll.setFourthAnswer("NOOOOOOOO!");
        instance.create(poll);

        instance.destroy(poll.getId());

        Poll result = instance.findPollByID(poll.getId());
        
        assertEquals(null, result);
    }

    /**
     * Test of findAllPolls method, of class PollJpaController.
     */
    @Test
    public void testFindAllPolls() {
        System.out.println("findAllPolls");

        int expResult = 3;
        List<Poll> result = instance.findAllPolls();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findPollEntities method, of class PollJpaController.
     */
    @Test
    public void testFindPollEntities() {
        System.out.println("findPollEntities");
        int maxResults = 5;
        int firstResult = 2;

        int expResult = 1;
        List<Poll> result = instance.findPollEntities(maxResults, firstResult);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of findPollByID method, of class PollJpaController.
     */
    @Test
    public void testFindPollByID() throws Exception {
        System.out.println("findPollByID");
        Poll poll = new Poll();
        poll.setQuestion("Do you like testing?");
        poll.setFirstAnswer("Yes");
        poll.setSecondAnswer("No");
        poll.setThirdAnswer("Not really");
        poll.setFourthAnswer("NOOOOOOOO!");
        instance.create(poll);

        Poll result = instance.findPollByID(poll.getId());
        assertEquals(poll, result);
    }

    /**
     * Test of getPollCount method, of class PollJpaController.
     */
    @Test
    public void testGetPollCount() {
        System.out.println("getPollCount");

        int expResult = 3;
        int result = instance.getPollCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of updatePoll method, of class PollJpaController.
     */
    @Test
    public void testUpdatePoll() throws Exception {
        System.out.println("updatePoll");
        int pollID = 1;
        int choice = 1;
        
        int expecResult = 1;

        instance.updatePoll(pollID, choice);

        Poll result = instance.findPollByID(pollID);
        
        assertEquals(expecResult, (int)result.getFirstCount());
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
