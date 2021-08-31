package facades;

import com.google.gson.Gson;
import dto.RecipeDTO;
import dto.RecipesDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.HttpUtils;

public class RecipeFacade {

    private static RecipeFacade instance;
    private static EntityManagerFactory emf;

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static RecipeFacade getRecipeFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RecipeFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private static final Gson GSON = new Gson();
    private static final String SINGLE_RECIPE_URL = "https://cphdat.dk/recipe/";
    private static final String RECIPE_LIST_URL = "https://cphdat.dk/recipes";

    /**
     * Method to search external api by Name
     * 
     * @param searchWord Search key
     * @return RecipesDTO of all recipes matching the search critiria
     */
    public RecipesDTO searchListByName(String searchWord) {
        RecipesDTO results = new RecipesDTO();
        RecipesDTO fullList = fetchExternalRecipeList();
        for (RecipeDTO recipe : fullList.getRecipes()) {
            if (recipe.getName().toLowerCase().contains(searchWord.toLowerCase())) {
                results.getRecipes().add(recipe);
            }
        }
        return results;
    }

    /**
     * Method to search external api by categories
     * 
     * UN-USED: This method is meant to be used as an alternative search method.
     * 
     * @param searchWord Search key
     * @return RecipesDTO of all recipes matching the search critiria
     */
    public RecipesDTO searchListByCategory(String searchWord) {
        RecipesDTO results = new RecipesDTO();
        RecipesDTO fullList = fetchExternalRecipeList();
        for (RecipeDTO recipe : fullList.getRecipes()) {
            if (recipe.getCategory().toLowerCase().contains(searchWord.toLowerCase())) {
                results.getRecipes().add(recipe);
            }
        }
        return results;
    }

    
    /**
     * Single fetch from external API of recipes.
     * 
     * @param recipeID
     * @return RecipeDTO
     */
    public RecipeDTO fetchSingleExternalRecipe(int recipeID) {
        try {
            String result = HttpUtils.fetchData(SINGLE_RECIPE_URL + recipeID);
            RecipeDTO recipe = GSON.fromJson(result, RecipeDTO.class);
            return recipe;
        } catch (IOException ex) {
            Logger.getLogger(RecipeFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /*
    I can't seem to get this to work, due to the fact that the list from the fetch is
    not a list, but an object with variables named 1, 2, 3..., and I can't seem to convert this
    to a list, no matter what method I try to implore
     */
//    public RecipesDTO fetchExternalRecipeList() {
//        try {
//            String result = HttpUtils.fetchData(RECIPE_LIST_URL);
//            RecipesDTO resultList = GSON.fromJson(result, RecipesDTO.class);
//            return resultList;
//        } catch (IOException ex) {
//            Logger.getLogger(RecipeFacade.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
    /*
    This is the fix, to show how the rest of the program works
     */
    public RecipesDTO fetchExternalRecipeList() {
        int[] listNumbers = new int[]{1, 2, 3, 4};
        return fetchMultipleExternalRecipes(listNumbers);
    }

    /**
     * Multiple parallel fetch from external API of recipes.
     * 
     * @param recipeIDs array of IDs
     * @return list of RecipeDTO
     */
    public RecipesDTO fetchMultipleExternalRecipes(int[] recipeIDs) {

        String[] fetchStrings = new String[recipeIDs.length];
        for (int i = 0; i < recipeIDs.length; i++) {
            fetchStrings[i] = SINGLE_RECIPE_URL + recipeIDs[i];
        }

        String[] fetched = new String[fetchStrings.length];
        ExecutorService workingJack = Executors.newFixedThreadPool(fetchStrings.length);

        try {
            for (int i = 0; i < fetchStrings.length; i++) {
                final int n = i;
                Runnable task = () -> {
                    try {
                        fetched[n] = HttpUtils.fetchData(fetchStrings[n]);
                    } catch (IOException ex) {
                        Logger.getLogger(RecipeFacade.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
                workingJack.submit(task);
            }

            workingJack.shutdown();
            workingJack.awaitTermination(5, TimeUnit.SECONDS);

            List<RecipeDTO> recipes = new ArrayList();
            for (String recipe : fetched) {
                recipes.add(GSON.fromJson(recipe, RecipeDTO.class));
            }
            RecipesDTO result = new RecipesDTO(recipes);
            return result;

        } catch (InterruptedException ex) {
            Logger.getLogger(RecipeFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
