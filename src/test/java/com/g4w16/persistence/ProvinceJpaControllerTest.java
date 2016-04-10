/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Province;
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
import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.Resource;
import javax.sql.DataSource;
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
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author Ofer Nitka-Nakash
 */
@RunWith(Arquillian.class)
@Ignore
public class ProvinceJpaControllerTest {
    
    @Inject
    private ProvinceJpaController provinceJpaController;

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
                .addPackage(ProvinceJpaController.class.getPackage())
                .addPackage(Province.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    
    /**
     * Test of create method, of class ProvinceJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        int originalCount = provinceJpaController.getProvinceCount();
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        int finalCount = provinceJpaController.getProvinceCount();
        assertThat(finalCount).isEqualTo(originalCount+1);
    }

    /**
     * Test of edit method, of class ProvinceJpaController.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
         int originalCount = provinceJpaController.getProvinceCount();
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        int finalCount = provinceJpaController.getProvinceCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        province.setId("NW");
        provinceJpaController.edit(province);
        assertThat("GW").isNotEqualTo(province.getId());
    }

    /**
     * Test of destroy method, of class ProvinceJpaController.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
         int originalCount = provinceJpaController.getProvinceCount();
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        int finalCount = provinceJpaController.getProvinceCount();
        if (originalCount + 1 != finalCount)
        {
            throw new Exception("Creation section of the editing test has failed");
        }
        provinceJpaController.destroy(province.getId());
        finalCount = provinceJpaController.getProvinceCount();
        assertThat(finalCount).isEqualTo(originalCount);
    }

    /**
     * Test of findProvinceEntities method, of class ProvinceJpaController.
     */
    @Test
    public void testFindProvinceEntities_0args() throws Exception{
        System.out.println("findProvinceEntities");
        List<Province> alreadyInTable = provinceJpaController.findProvinceEntities();
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        List<Province> expResult = new ArrayList<Province>();
        expResult.add(province);
        expResult.addAll(alreadyInTable);
        expResult.sort(new Comparator(){
                @Override
    public int compare(Object a, Object b) {
        return ((Province)a).getId().compareToIgnoreCase(((Province) b).getId());
    }
        });
        List<Province> result = provinceJpaController.findProvinceEntities();
        assertEquals(expResult, result);
    }

    /**
     * Test of findProvinceEntities method, of class ProvinceJpaController.
     */
    @Test
    public void testFindProvinceEntities_int_int() throws Exception {
        System.out.println("findProvinceEntities");
        int maxResults = 2;
        int firstResult = 0;
        Province province = new Province();
        province.setId("00");
        provinceJpaController.create(province);
        Province province1 = new Province();
        province1.setId("01");
        provinceJpaController.create(province1);
        Province province2 = new Province();
        province2.setId("LO");
        provinceJpaController.create(province2);
        List<Province> expResult = new ArrayList<Province>();
        expResult.add(province);
        expResult.add(province1);
        expResult.sort(new Comparator(){
                @Override
    public int compare(Object a, Object b) {
        return ((Province)a).getId().compareToIgnoreCase(((Province) b).getId());
    }
        });
        List<Province> result = provinceJpaController.findProvinceEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        //THIS test is problematic because entities are ordered by id and as such the entities you test for must be placed at the correct section
        // the solution I have used it to test for numeric id entities as they will always be sorted to the beginning and as such can be fetched by
        // starting at 0.
    }

    /**
     * Test of findProvince method, of class ProvinceJpaController.
     */
    @Test
    public void testFindProvince() throws Exception{
        System.out.println("findProvince");
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        Province province1 = new Province();
        province1.setId("PQ");
        provinceJpaController.create(province1);
        Province province2 = new Province();
        province2.setId("LO");
        provinceJpaController.create(province2);
        Province expResult = province1;
        Province result = provinceJpaController.findProvince(province1.getId());
        assertEquals(expResult, result);
    }

    /**
     * Test of getProvinceCount method, of class ProvinceJpaController.
     */
    @Test
    public void testGetProvinceCount() throws Exception{
        System.out.println("getProvinceCount");
        int alreadyInTable = provinceJpaController.getProvinceCount();
        Province province = new Province();
        province.setId("GW");
        provinceJpaController.create(province);
        Province province1 = new Province();
        province1.setId("PQ");
        provinceJpaController.create(province1);
        Province province2 = new Province();
        province2.setId("LO");
        provinceJpaController.create(province2);
        int expResult = 3 + alreadyInTable;
        int result = provinceJpaController.getProvinceCount();
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
