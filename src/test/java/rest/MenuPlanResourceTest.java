package rest;

import dto.IngredientDTO;
import dto.MenuPlanDTO;
import dto.MenuPlansDTO;
import dto.RecipeDTO;
import entities.DayPlan;
import entities.Ingredient;
import entities.MenuPlan;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

public class MenuPlanResourceTest {

    public MenuPlanResourceTest() {
    }

    private static DayPlan dayPlan;
    private static DayPlan[] dayPlans;
    private static Ingredient ing1, ing2, ing3;
    private static Ingredient[] ingredients;
    private static MenuPlan m1, m2;
    private static MenuPlan[] menuPlans;
    private static int highestMenuPlanID;
    private static RecipeDTO r1, r2;
    private static User user, admin;

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Ingredient").executeUpdate();
            em.createQuery("delete from DayPlan").executeUpdate();
            em.createQuery("delete from MenuPlan").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user = new User("user", "test");
            user.addRole(userRole);
            admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);

            ing1 = new Ingredient(25, "Black pepper");
            ing2 = new Ingredient(500, "Chicken");
            ing3 = new Ingredient(1, "Salt");
            
            r1 = new RecipeDTO("category 1", "Name 1", 40, 1, new String[]{"Do", "This", "Like", "That"},
                    new IngredientDTO[]{new IngredientDTO(ing1)});
            r2 = new RecipeDTO("category 2", "Name 2", 60, 2, new String[]{"Do", "That", "Like", "This"},
                    new IngredientDTO[]{new IngredientDTO(ing2), new IngredientDTO(ing3)});
            
            dayPlan = new DayPlan(r2.getId(), r2.getName(), 2);
            
            List<DayPlan> dayPlanList = Arrays.asList(new DayPlan[]{dayPlan});
            List<Ingredient> ingredientsList = Arrays.asList(new Ingredient[]{ing2, ing3});
            em.persist(dayPlan);
            em.persist(ing1);
            em.persist(ing2);
            em.persist(ing3);
            m1 = new MenuPlan(user, "Week 1");
            m2 = new MenuPlan(dayPlanList, user, ingredientsList, "Week 2");
            em.persist(m1);
            em.persist(m2);
            
            if(!user.getMenuPlans().contains(m1)){
                user.getMenuPlans().add(m1);
            }
            if(!user.getMenuPlans().contains(m2)){
                user.getMenuPlans().add(m2);
            }
            if(!Objects.equals(m1.getUser(), user)){
                m1.setUser(user);
            }
            if(!Objects.equals(m2.getUser(), user)){
                m2.setUser(user);
            }

            System.out.println("Saved test data to database");
            em.getTransaction().commit();

            dayPlans = new DayPlan[]{dayPlan};
            menuPlans = new MenuPlan[]{m1, m2};
            ingredients = new Ingredient[]{ing1, ing2, ing3};

            highestMenuPlanID = 0;
            for (MenuPlan menuPlan : menuPlans) {
                if (menuPlan.getId() > highestMenuPlanID) {
                    highestMenuPlanID = menuPlan.getId();
                }
            }

        } finally {
            em.close();
        }

    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testGetUserMenuPlans() {
        login("user", "test");
        MenuPlansDTO result = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/plan").then()
                .statusCode(200)
                .extract().body().as(MenuPlansDTO.class);
        
        assertNotNull(result);
        assertEquals(menuPlans.length, result.getMenuPlans().size());
    }

    @Test
    public void testGetALlMenuPlans() {
        login("admin", "test");
        MenuPlansDTO result = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/plan/all").then()
                .statusCode(200)
                .extract().body().as(MenuPlansDTO.class);
        
        assertNotNull(result);
        assertEquals(menuPlans.length, result.getMenuPlans().size());
    }

    @Test
    public void testAddNewDayPlan() {
        RecipeDTO recipe = r1;
        MenuPlan menuPlan = m2;
        int dayPlanAmount = menuPlan.getDayPlans().size();
        login("user", "test");
        MenuPlanDTO result = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(recipe)
                .when()
                .put("/plan?servings=3&menuPlanID=" + menuPlan.getId()).then()
                .statusCode(200)
                .extract().body().as(MenuPlanDTO.class);
        
        assertNotNull(result);
        assertEquals(dayPlanAmount, menuPlan.getDayPlans().size());
    }

    @Test
    public void testAddNewMenuPlan() {
        login("user", "test");
        MenuPlanDTO result = given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body("{\"menuName\": \"Week 1\"}")
                .when()
                .post("/plan").then()
                .statusCode(200)
                .extract().body().as(MenuPlanDTO.class);
        
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            user = em.find(User.class, user.getUserName());
            em.getTransaction().commit();
            assertEquals(menuPlans.length+1, user.getMenuPlans().size());
        } catch (Exception e){
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testDeleteMenuPlan() {
        MenuPlan menuPlan = m1;
        login("user", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/plan/" + m1.getId()).then()
                .statusCode(204);
        
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            user = em.find(User.class, user.getUserName());
            em.getTransaction().commit();
            assertEquals(menuPlans.length-1, user.getMenuPlans().size());
        } catch (Exception e){
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
        
    }

}
