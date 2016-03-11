/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g4w16.persistence;

import com.g4w16.entities.Client;
import com.g4w16.entities.Reviews;
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
import static org.assertj.core.api.Assertions.assertThat;
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
 * @author Dan 2016/2/25
 */
//@Ignore
@RunWith(Arquillian.class)
public class ClientJpaControllerTest {
    
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
                .addPackage(ClientJpaController.class.getPackage())
                .addPackage(Client.class.getPackage())
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
    private ClientJpaController clientJpaController;

    /**
     * Test of create method, of class ClientJpaController.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Client client = new Client();
        client.setId(101);
        client.setTitle("Mr");
        client.setFirstName("A");
        client.setLastName("B");
        client.setEmail("a@a.com");
        client.setPassword("a");
        client.setCompanyName("aaa");
        client.setCellPhoneNumber("123456789");
        client.setHomePhoneNumber("123456789");
        clientJpaController.create(client);
        int result = clientJpaController.getClientCount();
        assertThat(result).isEqualTo(101);
    }

    /**
     * Test of edit method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Client client = clientJpaController.findClientById(3);
        client.setFirstName("changed");
        clientJpaController.edit(client);
        Client new_client = clientJpaController.findClientById(3);
        assertThat(new_client.getFirstName()).isEqualTo("changed");
    }

    /**
     * Test of destroy method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = 1;
        clientJpaController.destroy(id);
        int result = clientJpaController.getClientCount();
        assertThat(result).isEqualTo(99);
    }

    /**
     * Test of findClientEntities method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientEntities_0args() {
        System.out.println("findClientEntities");
        List<Client> expResult = clientJpaController.findClientEntities();
        assertThat(expResult).hasSize(100);
    }

    /**
     * Test of findClientEntities method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientEntities_int_int() {
        System.out.println("findClientEntities");
        int maxResults = 10;
        int firstResult = 0;
        List<Client> expResult = clientJpaController.findClientEntities(maxResults, firstResult);
        assertThat(expResult).hasSize(10);
    }

    /**
     * Test of getClientCount method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testGetClientCount() {
        System.out.println("getClientCount");
        int result = clientJpaController.getClientCount();
        assertThat(result).isEqualTo(100);
    }

    /**
     * Test of findAllClients method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindAllClients() {
        System.out.println("findAllClients");
        List<Client> expResult = clientJpaController.findAllClients();
        assertThat(expResult).hasSize(100);
    }

    /**
     * Test of findClientById method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientById() {
        System.out.println("findClientById");
        int id = 1;
        Client expResult = clientJpaController.findClientById(id);
        assertThat(expResult.getId()).isEqualTo(id);
    }

    /**
     * Test of findClientByTitle method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByTitle() {
        System.out.println("findClientByTitle");
        List<Client> expResult = clientJpaController.findClientByTitle("Mrs");
        assertThat(expResult).hasSize(18);
    }

    /**
     * Test of findClientByFirstName method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByFirstName() {
        System.out.println("findClientByFirstName");
        List<Client> expResult = clientJpaController.findClientByFirstName("Robin");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findClientByLastName method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByLastName() {
        System.out.println("findClientByLastName");
        List<Client> expResult = clientJpaController.findClientByLastName("Phillips");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findClientByEmail method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByEmail() {
        System.out.println("findClientByEmail");
        List<Client> expResult = clientJpaController.findClientByEmail("rphillips0@phoca.cz");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findClientByCompanyName method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByCompanyName() {
        System.out.println("findClientByCompanyName");
        List<Client> expResult = clientJpaController.findClientByCompanyName("Brainsphere");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findClientByHomePhone method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByHomePhone() {
        System.out.println("findClientByHomePhone");
        List<Client> expResult = clientJpaController.findClientByHomePhone("1-(233)112-8684");
        assertThat(expResult).hasSize(1);
    }

    /**
     * Test of findClientByCellPhone method, of class ClientJpaController.
     */
    //@Ignore
    @Test
    public void testFindClientByCellPhone() {
        System.out.println("findClientByCellPhone");
        List<Client> expResult = clientJpaController.findClientByCellPhone("1-(954)156-1204");
        assertThat(expResult).hasSize(1);
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

