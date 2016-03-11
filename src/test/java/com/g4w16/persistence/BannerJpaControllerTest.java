/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Banner;
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
import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Ignore;

/**
 *
 * @author ofern
 */
@RunWith(Arquillian.class)
@Ignore
public class BannerJpaControllerTest {
    
     @Inject
    private BannerJpaController bannerJpaController;

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
                .addPackage(BannerJpaController.class.getPackage())
                .addPackage(Banner.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }

    /**
     * Test of create method, of class BannerJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Banner banner = new Banner();
        int originalCount = bannerJpaController.getBannerCount();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        int finalCount = bannerJpaController.getBannerCount();
        assertThat(finalCount).isEqualTo(originalCount+1);
    }

    /**
     * Test of edit method, of class BannerJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
         Banner banner = new Banner();
        int originalCount = bannerJpaController.getBannerCount();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        int finalCount = bannerJpaController.getBannerCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        banner.setUri("NewUri");
        bannerJpaController.edit(banner);
        assertThat("thisisauri").isNotEqualTo(banner.getUri());
    }

    /**
     * Test of destroy method, of class BannerJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
          Banner banner = new Banner();
        int originalCount = bannerJpaController.getBannerCount();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        int finalCount = bannerJpaController.getBannerCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        bannerJpaController.destroy(banner.getId());
        finalCount = bannerJpaController.getBannerCount();
        assertThat(finalCount).isEqualTo(originalCount);
    }

    /**
     * Test of findBannerEntities method, of class BannerJpaController.
     */
    @Test
    public void testFindBannerEntities_0args() throws Exception {
        System.out.println("findBannerEntities");
        Banner banner = new Banner();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        Banner banner2 = new Banner();
        banner2.setUri("thisisauri2");
        bannerJpaController.create(banner2);
        List<Banner> expResult = new ArrayList<Banner>();
        expResult.add(banner);
        expResult.add(banner2);
        List<Banner> result = bannerJpaController.findBannerEntities();
        assertEquals(expResult, result);
    }

    /**
     * Test of findBannerEntities method, of class BannerJpaController.
     */
    @Test
    public void testFindBannerEntities_int_int() throws Exception {
        System.out.println("findBannerEntities");
        int maxResults = 2;
        int firstResult = 0;
        Banner banner = new Banner();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        Banner banner1 = new Banner();
        banner1.setUri("thisisauri1");
        bannerJpaController.create(banner1);
        Banner banner2 = new Banner();
        banner2.setUri("thisisauri2");
        bannerJpaController.create(banner2);
        List<Banner> expResult = new ArrayList<Banner>();
        expResult.add(banner);
        expResult.add(banner1);
        List<Banner> result = bannerJpaController.findBannerEntities(maxResults, firstResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of findBanner method, of class BannerJpaController.
     */
    @Test
    public void testFindBanner() throws Exception{
        System.out.println("findBanner");
        Banner banner = new Banner();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        Banner banner1 = new Banner();
        banner1.setUri("thisisauri1");
        bannerJpaController.create(banner1);
        Banner banner2 = new Banner();
        banner2.setUri("thisisauri2");
        bannerJpaController.create(banner2);
        Banner expResult = banner1;
        Banner result = bannerJpaController.findBanner(banner1.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of getBannerCount method, of class BannerJpaController.
     */
    @Test
    public void testGetBannerCount() throws Exception{
        System.out.println("getBannerCount");
        Banner banner = new Banner();
        banner.setUri("thisisauri");
        bannerJpaController.create(banner);
        Banner banner1 = new Banner();
        banner1.setUri("thisisauri1");
        bannerJpaController.create(banner1);
        Banner banner2 = new Banner();
        banner2.setUri("thisisauri2");
        bannerJpaController.create(banner2);
        int expResult = 3;
        int result = bannerJpaController.getBannerCount();
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
