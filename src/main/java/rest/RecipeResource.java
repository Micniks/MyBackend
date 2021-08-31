package rest;

import com.google.gson.Gson;
import dto.RecipeDTO;
import dto.RecipesDTO;
import facades.RecipeFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("recipe")
public class RecipeResource {
    
    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);

    @Context
    private UriInfo context;

    public RecipeResource() {
    }
    
    private final static Gson GSON = new Gson();
    private static RecipeFacade facade = RecipeFacade.getRecipeFacade(EMF);

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipe(@PathParam("id") int id) {
        RecipeDTO recipe = facade.fetchSingleExternalRecipe(id);
        return GSON.toJson(recipe);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipes() {
        RecipesDTO recipe = facade.fetchExternalRecipeList();
        return GSON.toJson(recipe);
    }

        
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipes(@DefaultValue("") @QueryParam("name") String search) {
        RecipesDTO recipe = facade.searchListByName(search);
        return GSON.toJson(recipe);
    }
    
}
