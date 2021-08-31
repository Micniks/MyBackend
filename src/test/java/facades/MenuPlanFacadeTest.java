package facades;

import dto.IngredientDTO;
import dto.MenuPlanDTO;
import dto.RecipeDTO;
import entities.DayPlan;
import entities.Ingredient;
import entities.MenuPlan;
import entities.User;
import errorhandling.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

public class MenuPlanFacadeTest {

    private static EntityManagerFactory emf;
    private static MenuPlanFacade facade;
    private static User u1;
    private static DayPlan dayPlan;
    private static DayPlan[] dayPlans;
    private static Ingredient ing1, ing2, ing3;
    private static Ingredient[] ingredients;
    private static MenuPlan m1, m2;
    private static MenuPlan[] menuPlans;
    private static int highestMenuPlanID;
    private static RecipeDTO r1, r2;

    public MenuPlanFacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = MenuPlanFacade.getMenuPlanFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.createNamedQuery("Ingredient.deleteAllRows").executeUpdate();
            em.createNamedQuery("DayPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("MenuPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("users.deleteAllRows").executeUpdate();
            u1 = new User("Mike", "Password123");
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
            em.persist(u1);
            em.persist(dayPlan);
            em.persist(ing1);
            em.persist(ing2);
            em.persist(ing3);
            m1 = new MenuPlan(u1, "Week 1");
            m2 = new MenuPlan(dayPlanList, u1, ingredientsList, "Week 2");
            em.persist(m1);
            em.persist(m2);
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

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Ingredient.deleteAllRows").executeUpdate();
            em.createNamedQuery("DayPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("MenuPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("users.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testAddNewMenuPlan() {
        User user = u1;
        String menuName = "New Menu Plan";
        MenuPlanDTO result = facade.newMenuPlan(user.getUserName(), menuName);

        int expectedID = highestMenuPlanID + 1;
        assertEquals(expectedID, result.getId());
        assertEquals(user.getUserName(), result.getUser());
        assertEquals(menuName, result.getMenuName());
        assertTrue(result.getDayPlans().getDayPlans().isEmpty());
        assertTrue(result.getShoppingList().getIngredients().isEmpty());

        EntityManager em = emf.createEntityManager();
        try {
            List<MenuPlan> dbList = em.createQuery("Select m from MenuPlan m", MenuPlan.class)
                    .getResultList();
            int expectedDBSize = menuPlans.length + 1;
            assertEquals(expectedDBSize, dbList.size());
        } catch (Exception e) {
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // TODO: Delete or change this method 
    @Test
    public void testDeleteMenuPlan() {
        MenuPlan menuPlan = m2;
        int menuPlanID = menuPlan.getId();
        int amountOfDayPlans = menuPlan.getDayPlans().size();
        int amountofIngredients = menuPlan.getShoppingList().size();
        facade.DeleteMenuPlan(menuPlan.getId());

        EntityManager em = emf.createEntityManager();
        try {
            List<MenuPlan> dbList = em.createQuery("Select m from MenuPlan m", MenuPlan.class)
                    .getResultList();
            int expectedDBSize = menuPlans.length - 1;
            assertEquals(expectedDBSize, dbList.size());
            for (MenuPlan dbMenuPlan : dbList) {
                assertNotEquals(menuPlanID, dbMenuPlan.getId());
            }

            long dbDayPlansSize = (long) em.createQuery("Select COUNT(d) from DayPlan d")
                    .getSingleResult();
            int expectedDayPlansSize = dayPlans.length - amountOfDayPlans;
            assertEquals(expectedDayPlansSize, dbDayPlansSize);

            long dbIngredientsSize = (long) em.createQuery("Select COUNT(i) from Ingredient i")
                    .getSingleResult();
            int expectedIngredientsSize = ingredients.length - amountofIngredients;
            assertEquals(expectedIngredientsSize, dbIngredientsSize);

        } catch (Exception e) {
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testDeleteMenuPlan_EmptyMenuPlan() {
        MenuPlan menuPlan = m1;
        facade.DeleteMenuPlan(menuPlan.getId());
        assertTrue(menuPlan.getShoppingList().isEmpty());
        assertTrue(menuPlan.getDayPlans().isEmpty());

        EntityManager em = emf.createEntityManager();
        try {
            List<MenuPlan> dbList = em.createQuery("Select m from MenuPlan m", MenuPlan.class)
                    .getResultList();
            int expectedDBSize = menuPlans.length - 1;
            assertEquals(expectedDBSize, dbList.size());
        } catch (Exception e) {
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testDeleteMenuPlan_NotFound() {
        facade.DeleteMenuPlan(highestMenuPlanID + 10);

        EntityManager em = emf.createEntityManager();
        try {
            List<MenuPlan> dbList = em.createQuery("Select m from MenuPlan m", MenuPlan.class)
                    .getResultList();
            int expectedDBSize = menuPlans.length;
            assertEquals(expectedDBSize, dbList.size());
        } catch (Exception e) {
            fail("Something went wrong: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Test
    public void testAddDayPlan_ToEmptyMenuPlan() {
        try {
            MenuPlan menuPlan = m1;
            RecipeDTO recipe = r1;
            int DayPlanSize = menuPlan.getDayPlans().size();
            // There was already 2 of the 3 setup ingredients in the shopping cart, 
            // and this new one should add the 3rd ingredient from setup
            int ShoppingListSize = menuPlan.getShoppingList().size();
            IngredientDTO recipeIngredient = recipe.getIngredient_list()[0];

            int numberOfServings = 3;
            MenuPlanDTO result = facade.addDayPlan(menuPlan.getId(), numberOfServings, recipe);

            assertEquals(DayPlanSize + 1, result.getDayPlans().getDayPlans().size());
            assertEquals(ShoppingListSize + 1, result.getShoppingList().getIngredients().size());
            for (IngredientDTO ingredient : result.getShoppingList().getIngredients()) {
                boolean foundMatch = false;
                if (ingredient.getName().equals(recipeIngredient.getName())) {
                    foundMatch = true;
                    assertEquals(recipeIngredient.getAmount() * numberOfServings, ingredient.getAmount());
                }
                assertTrue(foundMatch);
            }

        } catch (NotFoundException ex) {
            Logger.getLogger(MenuPlanFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
