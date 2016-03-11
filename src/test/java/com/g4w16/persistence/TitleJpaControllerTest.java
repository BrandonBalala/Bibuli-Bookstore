/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Admin;
import com.g4w16.entities.Title;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import javax.annotation.Resource;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Ignore;

/**
 *
 * @author ofern
 */
@RunWith(Arquillian.class)
@Ignore
public class TitleJpaControllerTest {
    
    @Inject
    private TitleJpaController titleJpaController;
    
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
     * Test of create method, of class TitleJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Title title = new Title();
        int originalCount = titleJpaController.getTitleCount();
        title.setId("Sir");
        titleJpaController.create(title);
        int finalCount = titleJpaController.getTitleCount();
        assertThat(finalCount).isEqualTo(originalCount+1);
    }

    /**
     * Test of edit method, of class TitleJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        int originalCount = titleJpaController.getTitleCount();
        Title title = new Title();
        title.setId("Sir");
        titleJpaController.create(title);
        int finalCount = titleJpaController.getTitleCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        title.setId("new");
        titleJpaController.edit(title);
        assertThat("Sir").isNotEqualTo(title.getId());
    }

    /**
     * Test of destroy method, of class TitleJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        int originalCount = titleJpaController.getTitleCount();
        Title title = new Title();
        title.setId("Sir");
        titleJpaController.create(title);
        int finalCount = titleJpaController.getTitleCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        titleJpaController.destroy(title.getId());
        finalCount = titleJpaController.getTitleCount();
        assertThat(finalCount).isEqualTo(originalCount);
    }

    /**
     * Test of findTitleEntities method, of class TitleJpaController.
     */
    @Test
    public void testFindTitleEntities_0args() throws Exception{
        System.out.println("findTitleEntities");
        List<Title> alreadyInTable = titleJpaController.findTitleEntities();
        Title title = new Title();
        title.setId("Sir");
        titleJpaController.create(title);
        List<Title> expResult = new ArrayList<Title>();
        expResult.add(title);
        expResult.addAll(alreadyInTable);
        expResult.sort(new Comparator(){
                @Override
    public int compare(Object a, Object b) {
        return ((Title)a).getId().compareToIgnoreCase(((Title) b).getId());
    }
        });
        List<Title> result = titleJpaController.findTitleEntities();
        assertEquals(expResult, result);
    }

    /**
     * Test of findTitleEntities method, of class TitleJpaController.
     */
    @Test
    public void testFindTitleEntities_int_int() throws Exception {
        System.out.println("findTitleEntities");
        int maxResults = 2;
        int firstResult = 0;
        Title title = new Title();
        title.setId("00");
        titleJpaController.create(title);
        Title title1 = new Title();
        title1.setId("01");
        titleJpaController.create(title1);
        Title title2 = new Title();
        title2.setId("02");
        titleJpaController.create(title2);
        List<Title> expResult = new ArrayList<Title>();
        expResult.add(title);
        expResult.add(title1);
        expResult.sort(new Comparator(){
                @Override
    public int compare(Object a, Object b) {
        return ((Title)a).getId().compareToIgnoreCase(((Title) b).getId());
    }
        });
        List<Title> result = titleJpaController.findTitleEntities(maxResults, firstResult);
        assertEquals(expResult, result);
         //THIS test is problematic because entities are ordered by id and as such the entities you test for must be placed at the correct section
        // the solution I have used it to test for numeric id entities as they will always be sorted to the beginning and as such can be fetched by
        // starting at 0.
    }

    /**
     * Test of findTitle method, of class TitleJpaController.
     */
    @Test
    public void testFindTitle() throws Exception{
        System.out.println("findTitle");
        Title title = new Title();
        title.setId("AA");
        titleJpaController.create(title);
        Title title1 = new Title();
        title1.setId("AB");
        titleJpaController.create(title1);
        Title title2 = new Title();
        title2.setId("AC");
        titleJpaController.create(title2);
        Title expResult = title;
        Title result = titleJpaController.findTitle(title.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of getTitleCount method, of class TitleJpaController.
     */
    @Test
    public void testGetTitleCount() throws Exception{
        System.out.println("getTitleCount");
        int alreadyInTable = titleJpaController.getTitleCount();
        Title title = new Title();
        title.setId("00");
        titleJpaController.create(title);
        Title title1 = new Title();
        title1.setId("01");
        titleJpaController.create(title1);
        Title title2 = new Title();
        title2.setId("02");
        titleJpaController.create(title2);
        int expResult = 3 + alreadyInTable;
        int result = titleJpaController.getTitleCount();
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
