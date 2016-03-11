/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.persistence.exceptions.RollbackFailureException;
import com.g4w16.entities.Client;
import com.g4w16.entities.Reviews;
import com.g4w16.entities.ReviewsPK;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;

/**
 *
 * @author Dan 2016/2/24
 */
//@Ignore
@RunWith(Arquillian.class)
public class ReviewsJpaControllerTest {
    
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
                .addPackage(ReviewsJpaController.class.getPackage())
                .addPackage(Reviews.class.getPackage())
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
    private ReviewsJpaController reviewJpaController;
    
    
    /**
     * Test of create method, of class ReviewsJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateFormater.parse("2016-02-17 16:06:00");
        Reviews review = new Reviews();
        review.setText("hello");
        review.setRating(5);
        review.setCreationDate(date);
        review.setReviewsPK(new ReviewsPK(1, 1));
        reviewJpaController.create(review);
        int result = reviewJpaController.getReviewsCount();
        assertThat(result).isEqualTo(251);
    }

    /**
     * Test of edit method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        ReviewsPK pk=new ReviewsPK(1,20);
        Reviews reviews = reviewJpaController.findReviewByPK(pk);
        reviews.setRating(1);
        reviewJpaController.edit(reviews);
        Reviews new_review = reviewJpaController.findReviewByPK(pk);
        assertThat(new_review.getRating()).isEqualTo(1);
    }

    /**
     * Test of destroy method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        ReviewsPK pk=new ReviewsPK(1,20);
        reviewJpaController.destroy(pk);
        int result = reviewJpaController.getReviewsCount();
        assertThat(result).isEqualTo(249);
    }

    /**
     * Test of findReviewsEntities method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewsEntities_0args() {
        System.out.println("findReviewsEntities");
        List<Reviews> expResult = reviewJpaController.findReviewsEntities();
        assertThat(expResult).hasSize(250);
    }

    /**
     * Test of findReviewsEntities method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewsEntities_int_int() {
        System.out.println("findReviewsEntities");
        int maxResults = 10;
        int firstResult = 0;
        List<Reviews> expResult = reviewJpaController.findReviewsEntities(maxResults, firstResult);
        assertThat(expResult).hasSize(10);
    }

    /**
     * Test of findReviewByPK method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByPK(){
        System.out.println("findReviews");
        ReviewsPK id =new ReviewsPK(1,20);
        Reviews expResult = reviewJpaController.findReviewByPK(id);
        assertThat(expResult.getReviewsPK()).isEqualTo(id);
    }
   

    /**
     * Test of getReviewsCount method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testGetReviewsCount() {
        int result = reviewJpaController.getReviewsCount();
        assertThat(result).isEqualTo(250);
    }

    /**
     * Test of findAllReviews method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindAllReviews() {
        System.out.println("findAllReviews");
        List<Reviews> expResult = reviewJpaController.findAllReviews();
        assertThat(expResult).hasSize(250);
    }

    /**
     * Test of findReviewByBookID method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByBookID() {
        System.out.println("findReviewByBookID");
        int bookId=1;
        List<Reviews> expResult = reviewJpaController.findReviewByBookID(bookId);
        assertThat(expResult).hasSize(3);
    }

    /**
     * Test of findReviewByCreationDate method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByCreationDate() throws ParseException{
        System.out.println("findReviewByCreationDate");
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateFormater.parse("2015-12-24 00:00:00");
        List<Reviews> expResult = reviewJpaController.findReviewByCreationDate(date);
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findReviewByClientID method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByClientID() {
        System.out.println("findReviewByClientID");
        Client client = new Client();
        client.setId(6);
        List<Reviews> expResult = reviewJpaController.findReviewByClientID(client.getId());
        assertThat(expResult).hasSize(4);
    }

    /**
     * Test of findReviewByRateing method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByRateing() {
        System.out.println("findReviewByRateing");
        List<Reviews> expResult = reviewJpaController.findReviewByRateing(1);
        assertThat(expResult).hasSize(53);
    }

    /**
     * Test of findReviewByApprovalStatus method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testFindReviewByApprovalStatus() {
        System.out.println("findReviewByApprovalStatus");
        boolean approval = true;
        List<Reviews> expResult = reviewJpaController.findReviewByApprovalStatus(approval);
        assertThat(expResult).hasSize(131);
    }

    /**
     * Test of updateRemovalStatus method, of class ReviewsJpaController.
     */
    //@Ignore
    @Test
    public void testUpdateRemovalStatus() throws Exception {
        System.out.println("updateRemovalStatus");
        ReviewsPK pk=new ReviewsPK(1,20);
        reviewJpaController.updateRemovalStatus(pk);
        Boolean expResult = true;
        Boolean result = reviewJpaController.findReviewByPK(pk).getApproval();
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
